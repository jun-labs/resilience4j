package project.resilience4j.product.core.web.exception;

import lombok.Getter;
import project.resilience4j.common.codeandmessage.ErrorCodeAndMessage;
import project.resilience4j.common.exception.DomainException;

@Getter
public class ProductNotFoundException extends DomainException {

    public ProductNotFoundException(ErrorCodeAndMessage codeAndMessage) {
        super(codeAndMessage);
    }
}
