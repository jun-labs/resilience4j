package project.resilience4j.order.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.resilience4j.common.codeandmessage.ErrorCodeAndMessage;
import project.resilience4j.common.exception.CommonException;
import project.resilience4j.common.exception.DomainException;
import project.resilience4j.common.exception.RetryException;
import project.resilience4j.common.response.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> catchDomainException(DomainException exception) {
        ErrorCodeAndMessage codeAndMessage = exception.getCodeAndMessage();
        log.error("DomainException: {} resolved.", exception.getCodeAndMessage().getCode());
        return ResponseEntity.status(codeAndMessage.getStatusCode())
                .body(new ErrorResponse(codeAndMessage));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(CommonException.class)
    public ResponseEntity<ErrorResponse> catchCommonException(CommonException exception) {
        ErrorCodeAndMessage codeAndMessage = exception.getCodeAndMessage();
        log.error("CommonException: {} resolved.", exception.getCodeAndMessage().getCode());
        return ResponseEntity.status(codeAndMessage.getStatusCode())
                .body(new ErrorResponse(codeAndMessage));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(RetryException.class)
    public ResponseEntity<ErrorResponse> catchRetryException(RetryException exception) {
        log.error("RetryException resolved.");
        return ResponseEntity.status(502)
                .body(null);
    }
}
