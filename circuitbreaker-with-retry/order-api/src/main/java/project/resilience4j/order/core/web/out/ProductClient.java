package project.resilience4j.order.core.web.out;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.resilience4j.common.exception.BadGatewayException;
import project.resilience4j.order.core.web.dto.response.ProductResponse;
import project.resilience4j.order.core.web.exception.ProductNotFoundException;
import project.resilience4j.order.core.web.out.product.ProductSearchFeignClient;

import java.util.function.Supplier;

import static io.github.resilience4j.circuitbreaker.CircuitBreaker.decorateSupplier;
import static project.resilience4j.order.common.configuration.resilience4j.Resilience4jConfiguration.PRODUCT_SEARCH_RETRY_CONFIGURATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductClient {

    private final ProductSearchFeignClient productSearchFeignClient;
    private final RetryRegistry retryRegistry;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public ProductResponse findProductById(Long productId) {
        Retry retry = retryRegistry.retry(PRODUCT_SEARCH_RETRY_CONFIGURATION);
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(PRODUCT_SEARCH_RETRY_CONFIGURATION);
        Supplier<ProductResponse> productSearchSupplier =
                getProductResponseSupplier(productId, retry, circuitBreaker);

        try {
            return productSearchSupplier.get();
        } catch (BadGatewayException e) {
            log.error("The circuit has increased the failure count.");
            throw new ProductNotFoundException();
        }
    }

    private Supplier<ProductResponse> getProductResponseSupplier(
            Long productId,
            Retry retry,
            CircuitBreaker circuitBreaker
    ) {
        Supplier<ProductResponse> productSearchSupplier = Retry.decorateSupplier(retry, () -> callAPI(productId));
        productSearchSupplier = decorateSupplier(circuitBreaker, productSearchSupplier);
        return productSearchSupplier;
    }

    public ProductResponse callAPI(Long productId) {
        try {
            return productSearchFeignClient.findProduct(productId);
        } catch (FeignException exception) {
            if (exception.status() == 502) {
                log.error("Retry attempt.");
                throw new BadGatewayException();
            }
            throw new ProductNotFoundException();
        }
    }
}
