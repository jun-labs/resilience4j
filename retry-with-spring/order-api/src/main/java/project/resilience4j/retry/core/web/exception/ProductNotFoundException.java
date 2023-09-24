package project.resilience4j.retry.core.web.exception;

import lombok.Getter;
import project.resilience4j.retry.common.codeandmessage.ErrorCodeAndMessage;
import project.resilience4j.retry.common.exception.DomainException;

@Getter
public class ProductNotFoundException extends DomainException {

    public ProductNotFoundException(ErrorCodeAndMessage codeAndMessage) {
        super(codeAndMessage);
    }
}
