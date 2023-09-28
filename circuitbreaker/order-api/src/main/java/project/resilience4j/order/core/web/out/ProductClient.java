package project.resilience4j.order.core.web.out;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.resilience4j.order.common.configuration.resilience4j.Resilience4jConfig;
import project.resilience4j.order.core.web.dto.response.ProductResponse;
import project.resilience4j.order.core.web.out.search.ProductSearchFeignClient;
import project.resilience4j.order.core.web.exception.ProductNotFoundException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductClient {

    private final ProductSearchFeignClient productSearchFeignClient;

    @CircuitBreaker(name = Resilience4jConfig.PRODUCT_SEARCH_RETRY_CONFIGURATION, fallbackMethod = "searchProductFallback")
    public ProductResponse findProductById(Long productId) {
        ProductResponse response = callAPI(productId);
        if (response == null) {
            throw new ProductNotFoundException();
        }
        return response;
    }

    public ProductResponse callAPI(Long productId) {
        try {
            return productSearchFeignClient.findProduct(productId);
        } catch (FeignException exception) {
            return null;
        }
    }

    public ProductResponse searchProductFallback(
        Long productId,
        Throwable ex
    ) {
        throw new ProductNotFoundException();
    }
}
