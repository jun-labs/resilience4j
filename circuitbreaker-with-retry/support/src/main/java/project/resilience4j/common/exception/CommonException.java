package project.resilience4j.common.exception;

import lombok.Getter;
import project.resilience4j.common.codeandmessage.ErrorCodeAndMessage;

@Getter
public class CommonException extends RuntimeException {

    private final ErrorCodeAndMessage codeAndMessage;

    public CommonException(ErrorCodeAndMessage codeAndMessage) {
        super(codeAndMessage.getKrErrorMessage());
        this.codeAndMessage = codeAndMessage;
    }
}
