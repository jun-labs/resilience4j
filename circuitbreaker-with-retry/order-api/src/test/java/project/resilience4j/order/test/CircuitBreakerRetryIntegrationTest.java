package project.resilience4j.order.test;

import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import project.resilience4j.order.core.web.exception.ProductNotFoundException;
import project.resilience4j.order.core.web.out.ProductClient;
import project.resilience4j.order.core.web.out.product.ProductSearchFeignClient;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("[IntegrationTest] Retry 통합 테스트")
class CircuitBreakerRetryIntegrationTest {

    @MockBean
    private ProductSearchFeignClient productSearchFeignClient;

    @Autowired
    private ProductClient productClient;

    @Test
    @DisplayName("일정 횟수동안 재시도를 시도한다.")
    void retry_test() {
        Long productId = 502L;
        FeignException exception = createFeignException();

        when(productSearchFeignClient.findProduct(any()))
                .thenThrow(exception);

        assertThrows(ProductNotFoundException.class,
                () -> productClient.findProductById(productId));

        verify(productSearchFeignClient, times(3))
                .findProduct(any());
    }

    private FeignException createFeignException() {
        String message = "Bad Gateway";
        Request request = Request.create(
                Request.HttpMethod.GET,
                "https://hello-world/api",
                Collections.emptyMap(),
                null,
                Charset.defaultCharset()
        );
        byte[] body = new byte[0];
        Map<String, Collection<String>> headers = Collections.emptyMap();
        return new FeignException.BadGateway(message, request, body, headers);
    }
}
