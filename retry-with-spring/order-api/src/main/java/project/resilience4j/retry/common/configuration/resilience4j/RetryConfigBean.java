package project.resilience4j.retry.common.configuration.resilience4j;

import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.resilience4j.retry.common.exception.RetryException;

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
}
