package project.resilience4j.common.exception;

import static project.resilience4j.common.codeandmessage.common.CommonErrorCodeAndMessage.RETRY;

public class RetryException extends CommonException {

    public RetryException() {
        super(RETRY);
    }
}
