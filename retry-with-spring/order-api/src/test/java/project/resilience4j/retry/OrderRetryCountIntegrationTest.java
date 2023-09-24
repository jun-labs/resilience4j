package project.resilience4j.retry;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import feign.FeignException.NotFound;
import feign.Request;
import feign.Request.HttpMethod;
import feign.RequestTemplate;
import java.util.HashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import project.resilience4j.retry.core.web.exception.ProductNotFoundException;
import project.resilience4j.retry.core.web.out.ProductClient;
import project.resilience4j.retry.core.web.out.search.ProductSearchFeignClient;

@SpringBootTest
@ActiveProfiles("test")
class OrderRetryCountIntegrationTest {

    @SpyBean
    private ProductSearchFeignClient feignClient;

    @Autowired
    private ProductClient productClient;

    @Test
    @DisplayName("상품 조회가 실패하면 최대 3번까지 재시도 한다.")
    void retry_count_test() {
        Long invalidProductId = Long.MAX_VALUE;
        Request request = createRequest();

        doThrow(createNotFoundException(request))
            .when(feignClient).findProduct(invalidProductId);

        assertThrows(ProductNotFoundException.class,
            () -> productClient.findProductById(invalidProductId));

        verify(feignClient, times(3))
            .findProduct(invalidProductId);
    }

    private NotFound createNotFoundException(Request request) {
        return new NotFound("", request, null, null);
    }

    private Request createRequest() {
        return Request.create(
            HttpMethod.GET,
            "localhost:8080",
            new HashMap<>(),
            Request.Body.empty(),
            new RequestTemplate()
        );
    }
}
