package project.resilience4j.order.test;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.State;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import project.resilience4j.order.core.web.exception.ProductNotFoundException;
import project.resilience4j.order.core.web.out.ProductClient;
import project.resilience4j.order.core.web.out.product.ProductSearchFeignClient;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static project.resilience4j.order.common.configuration.resilience4j.Resilience4jConfiguration.PRODUCT_SEARCH_RETRY_CONFIGURATION;

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

    @AfterEach
    void setUp() {
        CircuitBreaker circuitBreaker =
                circuitBreakerRegistry.circuitBreaker(PRODUCT_SEARCH_RETRY_CONFIGURATION);

        circuitBreaker.reset();
    }

    @Test
    @DisplayName("일정 횟수 이상 에러가 발생하면 서킷이 OPEN 된다.")
    void circuit_status_open_test() throws InterruptedException {
        Long productId = 50L;
        when(productSearchFeignClient.findProduct(any()))
                .thenThrow(new ProductNotFoundException());

        CircuitBreaker circuitBreaker =
                circuitBreakerRegistry.circuitBreaker(PRODUCT_SEARCH_RETRY_CONFIGURATION);

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i = 1; i <= 5; i++) {
            executorService.submit(() -> {
                try {
                    productClient.findProductById(productId);
                } catch (Exception exception) {
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        assertEquals(State.OPEN, circuitBreaker.getState());
    }
}
