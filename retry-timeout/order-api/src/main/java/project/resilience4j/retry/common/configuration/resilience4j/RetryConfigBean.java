package project.resilience4j.retry.common.configuration.resilience4j;

import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.resilience4j.retry.common.exception.RetryException;

import java.time.Duration;

@Configuration
public class RetryConfigBean {

    public static final String PRODUCT_SEARCH_RETRY_CONFIGURATION = "productSearchRetryConfiguration";

    @Bean
    public RetryConfig productSearchRetryConfiguration() {
        return RetryConfig.custom()
                .retryOnException(RetryException.class::isInstance)
                .waitDuration(Duration.ofMillis(1000))
                .maxAttempts(3)
                .build();
    }

    @Bean
    public RetryRegistry retryRegistry() {
        return RetryRegistry.of(productSearchRetryConfiguration());
    }

    @Bean
    public TimeLimiter timeLimiter() {
        return TimeLimiter.of(timeLimiterConfig());
    }

    @Bean
    public TimeLimiterConfig timeLimiterConfig() {
        return TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(4))
                .cancelRunningFuture(true)
                .build();
    }
}
