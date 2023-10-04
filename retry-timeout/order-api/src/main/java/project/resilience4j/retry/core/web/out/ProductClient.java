package project.resilience4j.retry.core.web.out;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.resilience4j.retry.common.exception.RequestTimeoutException;
import project.resilience4j.retry.common.exception.RetryException;
import project.resilience4j.retry.core.web.dto.response.ProductResponse;
import project.resilience4j.retry.core.web.exception.ProductNotFoundException;
import project.resilience4j.retry.core.web.out.search.ProductSearchFeignClient;

import java.util.concurrent.*;
import java.util.function.Supplier;

import static project.resilience4j.retry.common.configuration.resilience4j.RetryConfigBean.PRODUCT_SEARCH_RETRY_CONFIGURATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductClient {

    private final ProductSearchFeignClient productSearchFeignClient;
    private final RetryRegistry retryRegistry;
    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final TimeLimiter timeLimiter;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(8);

    public ProductResponse findProductById(Long productId) {
        Retry retry = retryRegistry.retry(PRODUCT_SEARCH_RETRY_CONFIGURATION);
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(PRODUCT_SEARCH_RETRY_CONFIGURATION);

        Supplier<CompletableFuture<ProductResponse>> productSupplier =
                () -> CompletableFuture.supplyAsync(() -> productSearchFeignClient.findProduct(productId));

        Supplier<CompletableFuture<ProductResponse>> timeLimiterSupplier =
                () -> timeLimiter.executeCompletionStage(scheduler, productSupplier).toCompletableFuture();

        Supplier<ProductResponse> decoratedSupplier = Retry.decorateSupplier(
                retry,
                CircuitBreaker.decorateSupplier(circuitBreaker,
                        () -> execute(timeLimiterSupplier)
                )
        );
        return getProductResponse(decoratedSupplier);
    }

    private ProductResponse execute(
            Supplier<CompletableFuture<ProductResponse>> supplier
    ) {
        try {
            return supplier.get().get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException exception) {
            Throwable throwable = exception.getCause();
            if (throwable instanceof FeignException && ((FeignException) throwable).status() == 404) {
                log.error("Retry attempt.");
                Retry retry = retryRegistry.retry(PRODUCT_SEARCH_RETRY_CONFIGURATION);
                if (retry.getMetrics().getNumberOfFailedCallsWithRetryAttempt() >= retry.getRetryConfig().getMaxAttempts() - 1) {
                    throw new ProductNotFoundException();
                }
                throw new RetryException("Retry attempt.");
            } else if (throwable instanceof TimeoutException) {
                throw new RequestTimeoutException();
            }
            throw new RuntimeException(throwable);
        }
    }

    private static ProductResponse getProductResponse(
            Supplier<ProductResponse> supplier
    ) {
        try {
            return supplier.get();
        } catch (RetryException exception) {
            log.error("The circuit has increased the failure count.");
            throw new ProductNotFoundException();
        }
    }
}
