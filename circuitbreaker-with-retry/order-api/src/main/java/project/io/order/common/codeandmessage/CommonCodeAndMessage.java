package project.io.order.common.codeandmessage;

public enum CommonCodeAndMessage implements CodeAndMessage {
    OK(200, "OK"),
    CREATED(201, "CREATED"),
    INVALID_CLIENT_REQUEST(400, "올바르지 않은 요청입니다."),
    API_SPEC_UN_MATCHED(400, "외부 서버 API 스펙과 매핑을 확인해주세요."),
    INVALID_PATH_VARIABLE(400, "올바른 PATH 경로를 확인해주세요."),
    INVALID_QUERY_PARAMETERS(400, "올바른 파라미터를 입력해주세요."),
    UN_AUTHORIZED(401, "권한이 존재하지 않습니다."),
    URL_NOTFOUND(404, "존재하지 않는 URL 입니다."),
    INTERNAL_SERVER_ERROR(500, "서버 내부 오류입니다."),
    BAD_GATEWAY(502, "외부 서버와 통신하는 과정에서 오류가 발생했습니다."),
    RETRY_REQUEST(600, "일시적 오류로 재시도 합니다.");

    private final int code;
    private final String message;

    CommonCodeAndMessage(
        int code,
        String message
    ) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public int code() {
        return code;
    }
}
