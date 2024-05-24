package project.io.order.common.configuration.resilience4j;

import feign.*;
import io.github.resilience4j.circuitbreaker.*;
import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.*;
import io.github.resilience4j.core.*;
import io.github.resilience4j.retry.*;
import lombok.extern.slf4j.*;
import org.springframework.context.annotation.*;
import project.io.order.common.exception.*;
import project.io.order.core.web.exception.*;

import javax.naming.*;
import java.time.*;
import java.util.concurrent.*;

@Slf4j
@Configuration
public class Resilience4jConfiguration {

    public static final String RESILIENCE4J_CONFIGURATION = "resilience4jConfiguration";

    @Bean
    public RetryConfig retryConfiguration() {
        IntervalFunction intervalFunction =
            IntervalFunction.ofExponentialBackoff(Duration.ofMillis(1_000), 1.5);
        return RetryConfig.custom()
            .retryOnException(throwable ->
                throwable instanceof FeignException ||
                    throwable instanceof BadGatewayException ||
                    throwable instanceof TimeoutException ||
                    throwable instanceof ServiceUnavailableException
            )
            .intervalFunction(intervalFunction)
            .maxAttempts(3)
            .build();
    }

    @Bean
    public RetryRegistry retryRegistry(RetryConfig retryConfig) {
        RetryRegistry registry = RetryRegistry.of(retryConfig);
        registry.retry(RESILIENCE4J_CONFIGURATION).getEventPublisher()
            .onRetry(event -> log.info("Retry attempted for {}, attempt number: {}", event.getName(), event.getNumberOfRetryAttempts()))
            .onError(event -> log.error("Retry failed after several attempts for {}", event.getName(), event.getLastThrowable()))
            .onSuccess(event -> log.info("Retry succeeded for {}", event.getName()));
        return registry;
    }

    @Bean
    public CircuitBreaker circuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
        return circuitBreakerRegistry.circuitBreaker(
            RESILIENCE4J_CONFIGURATION,
            CircuitBreakerConfig.custom()
                .recordException(ProductTypeException.class::isInstance)
                .failureRateThreshold(50)
                .slidingWindowType(TIME_BASED)
                .slidingWindowSize(20)
                .minimumNumberOfCalls(50)
                .waitDurationInOpenState(Duration.ofSeconds(30))
                .build()
        );
    }
}
