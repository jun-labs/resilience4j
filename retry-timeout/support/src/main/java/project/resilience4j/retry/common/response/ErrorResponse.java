package project.resilience4j.retry.common.response;

import lombok.Getter;
import project.resilience4j.retry.common.codeandmessage.ErrorCodeAndMessage;

@Getter
public class ErrorResponse {

    private int errorCode;
    private String message;

    public ErrorResponse(ErrorCodeAndMessage codeAndMessage) {
        this.errorCode = codeAndMessage.getStatusCode();
        this.message = codeAndMessage.getKrErrorMessage();
    }
}
