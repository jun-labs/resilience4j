package project.resilience4j.retry.core.web.exception;

import lombok.Getter;
import project.resilience4j.retry.common.codeandmessage.ErrorCodeAndMessage;
import project.resilience4j.retry.common.exception.DomainException;

import static project.resilience4j.retry.common.codeandmessage.product.ProductErrorCodeAndMessage.PRODUCT_NOT_FOUND;

@Getter
public class ProductNotFoundException extends DomainException {

    public ProductNotFoundException() {
        super(PRODUCT_NOT_FOUND);
    }

    public ProductNotFoundException(ErrorCodeAndMessage codeAndMessage) {
        super(codeAndMessage);
    }
}
