package project.resilience4j.retrywithoutspring;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    private static final int MAX_RETRY_COUNT = 5;
    private static final int ONE_SECONDS = 1000;

    public static void main(String[] args) throws Exception {
        process("HELLO");
    }

    public static String process(String parameter) throws InterruptedException {
        String result = null;

        int retryCount = 0;
        while (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            try {
                result = callAPI(parameter);
            } catch (RetryException exception) {
                if (retryCount == MAX_RETRY_COUNT) {
                    return fallback(parameter, exception);
                }
                Thread.sleep(ONE_SECONDS);
            }
            if (result != null) {
                break;
            }
        }
        return result;
    }

    private static String fallback(
        String parameter,
        Exception exception
    ) {
        log.info("Request: {}", parameter);
        return "Recovered: " + exception.toString();
    }

    private static String callAPI(String param) {
        log.error(String.format("Error, parameter is %s", param));
        throw new RetryException("Invalid Parameter.");
    }
}
