package project.resilience4j.order.core.web.exception;

import lombok.Getter;
import project.resilience4j.common.codeandmessage.ErrorCodeAndMessage;
import project.resilience4j.common.codeandmessage.product.ProductErrorCodeAndMessage;
import project.resilience4j.common.exception.DomainException;

@Getter
public class ProductNotFoundException extends DomainException {

    public ProductNotFoundException() {
        super(ProductErrorCodeAndMessage.PRODUCT_NOT_FOUND);
    }

    public ProductNotFoundException(ErrorCodeAndMessage codeAndMessage) {
        super(codeAndMessage);
    }
}
