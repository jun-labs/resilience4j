package project.resilience4j.retry.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import project.resilience4j.retry.common.exception.RequestTimeoutException;
import project.resilience4j.retry.core.web.out.ProductClient;
import project.resilience4j.retry.core.web.out.search.ProductSearchFeignClient;

import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class OrderRetryCountIntegrationTest {

    @SpyBean
    private ProductSearchFeignClient feignClient;

    @Autowired
    private ProductClient productClient;

    @Test
    @DisplayName("TimeoutException이 발생하면 Retry를 하지 않는다.")
    void retry_count_test() {
        Long invalidProductId = Long.MAX_VALUE;

        doThrow(new CompletionException(new TimeoutException()))
                .when(feignClient).findProduct(invalidProductId);

        assertThrows(RequestTimeoutException.class,
                () -> productClient.findProductById(invalidProductId));

        verify(feignClient, times(1))
                .findProduct(invalidProductId);
    }
}
