package project.resilience4j.retry.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.resilience4j.retry.common.codeandmessage.ErrorCodeAndMessage;
import project.resilience4j.retry.common.response.ErrorResponse;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> catchDomainException(DomainException exception) {
        ErrorCodeAndMessage codeAndMessage = exception.getCodeAndMessage();
        return ResponseEntity.status(codeAndMessage.getStatusCode())
            .body(new ErrorResponse(codeAndMessage));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(RequestTimeoutException.class)
    public ResponseEntity<ErrorResponse> catchDomainException(RequestTimeoutException exception) {
        ErrorCodeAndMessage codeAndMessage = exception.getCodeAndMessage();
        return ResponseEntity.status(codeAndMessage.getStatusCode())
                .body(new ErrorResponse(codeAndMessage));
    }
}
