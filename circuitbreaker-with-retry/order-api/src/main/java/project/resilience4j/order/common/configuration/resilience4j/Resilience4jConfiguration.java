package project.resilience4j.order.common.configuration.resilience4j;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryException;
import project.resilience4j.common.exception.BadGatewayException;
import project.resilience4j.order.core.web.exception.ProductNotFoundException;

import java.time.Duration;

import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.COUNT_BASED;

@Slf4j
@Configuration
public class Resilience4jConfiguration {

    public static final String PRODUCT_SEARCH_RETRY_CONFIGURATION = "productSearchRetryConfiguration";

    @Bean
    public RetryConfig productSearchRetryConfiguration() {
        IntervalFunction intervalFunction =
                IntervalFunction.ofExponentialBackoff(Duration.ofMillis(1000), 1.5);
        return RetryConfig.custom()
                .retryOnException(BadGatewayException.class::isInstance)
                .intervalFunction(intervalFunction)
                .maxAttempts(3)
                .build();
    }

    @Bean
    public RetryRegistry retryRegistry() {
        return RetryRegistry.of(productSearchRetryConfiguration());
    }

    @Bean
    public CircuitBreaker circuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
        return circuitBreakerRegistry.circuitBreaker(
                PRODUCT_SEARCH_RETRY_CONFIGURATION,
                CircuitBreakerConfig.custom()
                        .recordExceptions(ProductNotFoundException.class)
                        .ignoreExceptions(RetryException.class, BadGatewayException.class)
                        .failureRateThreshold(20)
                        .slidingWindowType(COUNT_BASED)
                        .slidingWindowSize(10)
                        .minimumNumberOfCalls(5)
                        .waitDurationInOpenState(Duration.ofSeconds(10))
                        .build()
        );
    }
}
