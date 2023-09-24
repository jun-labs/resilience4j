package project.resilience4j.retry.common.utils;

import static project.resilience4j.retry.common.codeandmessage.product.ProductErrorCodeAndMessage.PRODUCT_NOT_FOUND;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import project.resilience4j.retry.common.exception.RetryException;
import project.resilience4j.retry.core.web.exception.ProductNotFoundException;

@Slf4j
public final class FeignClientUtils {

    private FeignClientUtils() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static void throwException(FeignException exception) {
        if (exception.status() == 404) {
            log.error("Error occurred. Retry attempt.");
            throw new RetryException("Retry attempt.");
        }
    }
}
