package project.resilience4j.order;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static project.resilience4j.order.common.configuration.resilience4j.Resilience4jConfig.PRODUCT_SEARCH_RETRY_CONFIGURATION;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.State;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import project.resilience4j.order.core.web.exception.ProductNotFoundException;
import project.resilience4j.order.core.web.out.ProductClient;
import project.resilience4j.order.core.web.out.search.ProductSearchFeignClient;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("[IntegrationTest] CircuitBreaker 통합 테스트")
class CircuitBreakerStatusIntegrationTest {

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @MockBean
    private ProductSearchFeignClient productSearchFeignClient;

    @Autowired
    private ProductClient productClient;

    @Test
    @DisplayName("일정 횟수 이상 에러가 발생하면 서킷이 OPEN 된다.")
    void circuit_status_open_test() {
        Long productId = 1L;
        when(productSearchFeignClient.findProduct(any()))
            .thenThrow(new ProductNotFoundException());

        CircuitBreaker circuitBreaker =
            circuitBreakerRegistry.circuitBreaker(PRODUCT_SEARCH_RETRY_CONFIGURATION);

        for (int i = 1; i <= 100; i++) {
            try {
                productClient.findProductById(productId);
            } catch (ProductNotFoundException exception) {
            }
        }

        assertSame(State.OPEN, circuitBreaker.getState());
    }
}
