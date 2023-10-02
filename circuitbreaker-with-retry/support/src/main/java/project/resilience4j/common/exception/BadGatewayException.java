package project.resilience4j.common.exception;

import static project.resilience4j.common.codeandmessage.common.CommonErrorCodeAndMessage.BAD_GATEWAY;

public class BadGatewayException extends CommonException {

    public BadGatewayException() {
        super(BAD_GATEWAY);
    }
}
