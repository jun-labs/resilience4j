package project.resilience4j.retry.common.exception;

import lombok.Getter;
import project.resilience4j.retry.common.codeandmessage.ErrorCodeAndMessage;
import project.resilience4j.retry.common.codeandmessage.common.CommonErrorCodeAndMessage;

@Getter
public class RequestTimeoutException extends RuntimeException{

    private final ErrorCodeAndMessage errorCodeAndMessage;

    public RequestTimeoutException() {
        this.errorCodeAndMessage = CommonErrorCodeAndMessage.REQUEST_TIMEOUT;
    }
}
