package project.resilience4j.order.core.web.external;

import feign.*;
import io.github.resilience4j.circuitbreaker.annotation.*;
import io.github.resilience4j.retry.annotation.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.stereotype.*;
import project.resilience4j.order.common.exception.*;
import project.resilience4j.order.core.web.dto.response.*;
import project.resilience4j.order.core.web.exception.*;
import project.resilience4j.order.core.web.external.product.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductClient {

    private final ProductSearchFeignClient productSearchFeignClient;

    @Retry(name = "productSearchRetryConfiguration", fallbackMethod = "fallbackAfterRetries")
    @CircuitBreaker(name = "productSearchRetryConfiguration", fallbackMethod = "fallbackAfterCircuitBreaker")
    public ProductResponse findProductById(Long productId) {
        try {
            return productSearchFeignClient.findProduct(productId);
        } catch (FeignException e) {
            if (e.status() == 502) {
                throw new BadGatewayException();
            }
            throw new ProductNotFoundException();
        }
    }

    private ProductResponse fallbackAfterRetries(
        Long productId,
        Throwable throwable
    ) {
        log.error("All retries failed for ID: {} due to: {}", productId, throwable.getMessage());
        throw new ProductNotFoundException();
    }

    private ProductResponse fallbackAfterCircuitBreaker(Long productId, Throwable throwable) {
        log.error("Circuit breaker opened for ID: {} due to: {}", productId, throwable.getMessage());
        throw new ProductNotFoundException();
    }
}
