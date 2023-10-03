package project.resilience4j.retry.common.exception;

public class RetryException extends RuntimeException {

    public RetryException(String message) {
        super(message);
    }
}
