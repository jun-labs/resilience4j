package project.resilience4j.order.common.configuration.resilience4j;

import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.COUNT_BASED;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.resilience4j.common.exception.DomainException;

@Slf4j
@Configuration
public class Resilience4jConfig {

    public static final String PRODUCT_SEARCH_RETRY_CONFIGURATION = "productSearchRetryConfiguration";

    @Bean
    public CircuitBreaker circuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
        return circuitBreakerRegistry.circuitBreaker(
            PRODUCT_SEARCH_RETRY_CONFIGURATION,
            CircuitBreakerConfig.custom()
                .recordExceptions(DomainException.class)
                .failureRateThreshold(20)
                .slidingWindowType(COUNT_BASED)
                .slidingWindowSize(10)
                .minimumNumberOfCalls(5)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .build()
        );
    }
}
