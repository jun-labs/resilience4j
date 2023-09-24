package project.resilience4j.retrywithoutspring;

public class RetryException extends RuntimeException {

    public RetryException(String message) {
        super(message);
    }
}
