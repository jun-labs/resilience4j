package project.resilience4j.order.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.resilience4j.common.codeandmessage.ErrorCodeAndMessage;
import project.resilience4j.common.exception.DomainException;
import project.resilience4j.common.response.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> catchDomainException(DomainException exception) {
        ErrorCodeAndMessage codeAndMessage = exception.getCodeAndMessage();
        log.error("DomainException resolved.");
        return ResponseEntity.status(codeAndMessage.getStatusCode())
            .body(new ErrorResponse(codeAndMessage));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(RetryException.class)
    public ResponseEntity<ErrorResponse> catchRetryException(RetryException exception) {
        log.error("DomainException resolved.");
        return ResponseEntity.status(404)
            .body(null);
    }
}
