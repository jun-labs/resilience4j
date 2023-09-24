package project.resilience4j.retry.common.exception;

import lombok.Getter;
import project.resilience4j.retry.common.codeandmessage.ErrorCodeAndMessage;

@Getter
public class DomainException extends RuntimeException {

    private final ErrorCodeAndMessage codeAndMessage;

    public DomainException(ErrorCodeAndMessage codeAndMessage) {
        super(codeAndMessage.getKrErrorMessage());
        this.codeAndMessage = codeAndMessage;
    }
}
