package project.resilience4j.retry.common.codeandmessage.common;

import project.resilience4j.retry.common.codeandmessage.CodeAndMessage;

public enum SuccessCodeAndMessage implements CodeAndMessage {
    OK(
        200,
        "OK"
    ),
    CREATED(
        201,
        "CREATED"
    );

    private final int code;
    private final String statusCode;

    SuccessCodeAndMessage(
        int code,
        String statusCode
    ) {
        this.code = code;
        this.statusCode = statusCode;
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public int getStatusCode() {
        return code;
    }
}
