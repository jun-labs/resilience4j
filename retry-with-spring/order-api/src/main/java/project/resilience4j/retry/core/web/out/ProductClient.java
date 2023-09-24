package project.resilience4j.retry.core.web.out;

import static project.resilience4j.retry.common.codeandmessage.product.ProductErrorCodeAndMessage.PRODUCT_NOT_FOUND;
import static project.resilience4j.retry.common.configuration.resilience4j.RetryConfigBean.PRODUCT_SEARCH_RETRY_CONFIGURATION;
import feign.FeignException;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.resilience4j.retry.common.utils.FeignClientUtils;
import project.resilience4j.retry.core.web.dto.response.ProductResponse;
import project.resilience4j.retry.core.web.exception.ProductNotFoundException;
import project.resilience4j.retry.core.web.out.search.ProductSearchFeignClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductClient {

    private final ProductSearchFeignClient productSearchFeignClient;

    @Retry(name = PRODUCT_SEARCH_RETRY_CONFIGURATION, fallbackMethod = "fallback")
    public ProductResponse findProductById(Long productId) {
        try {
            log.info("[Order/Client]----xx> Order Request: {}", productId);
            return productSearchFeignClient.findProduct(productId);
        } catch (FeignException exception) {
            FeignClientUtils.throwException(exception);
        }
        throw new RuntimeException("올바르지 않은 요청입니다.");
    }

    private ProductResponse fallback(
        Long productId,
        Exception exception
    ) {
        throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
    }
}
