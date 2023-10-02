package project.resilience4j.product.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.resilience4j.common.codeandmessage.ErrorCodeAndMessage;
import project.resilience4j.common.exception.DomainException;
import project.resilience4j.common.exception.BadGatewayException;
import project.resilience4j.common.response.ErrorResponse;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> catchDomainException(DomainException domainException) {
        ErrorCodeAndMessage codeAndMessage = domainException.getCodeAndMessage();
        return ResponseEntity.status(codeAndMessage.getStatusCode())
            .body(new ErrorResponse(codeAndMessage));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BadGatewayException.class)
    public ResponseEntity<ErrorResponse> catchTimeoutException(BadGatewayException domainException) {
        ErrorCodeAndMessage codeAndMessage = domainException.getCodeAndMessage();
        return ResponseEntity.status(codeAndMessage.getStatusCode())
            .body(new ErrorResponse(codeAndMessage));
    }
}
