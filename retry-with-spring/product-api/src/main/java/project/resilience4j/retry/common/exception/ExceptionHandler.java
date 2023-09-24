package project.resilience4j.retry.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.resilience4j.retry.common.codeandmessage.ErrorCodeAndMessage;
import project.resilience4j.retry.common.response.ErrorResponse;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> catchDomainException(DomainException domainException) {
        ErrorCodeAndMessage codeAndMessage = domainException.getCodeAndMessage();
        return ResponseEntity.status(codeAndMessage.getStatusCode())
            .body(new ErrorResponse(codeAndMessage));
    }
}
