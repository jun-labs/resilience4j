package project.resilience4j.retry.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.resilience4j.retry.common.codeandmessage.ErrorCodeAndMessage;
import project.resilience4j.retry.common.codeandmessage.common.CommonErrorCodeAndMessage;
import project.resilience4j.retry.common.response.ErrorResponse;

import static org.springframework.http.HttpStatus.REQUEST_TIMEOUT;

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
        log.error("RetryException resolved.");
        return ResponseEntity.status(REQUEST_TIMEOUT.value())
                .body(new ErrorResponse(CommonErrorCodeAndMessage.REQUEST_TIMEOUT));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(RequestTimeoutException.class)
    public ResponseEntity<ErrorResponse> catchTimeoutException(RequestTimeoutException exception) {
        log.error("TimeoutException resolved.");
        return ResponseEntity.status(REQUEST_TIMEOUT.value())
                .body(new ErrorResponse(CommonErrorCodeAndMessage.REQUEST_TIMEOUT));
    }
}
