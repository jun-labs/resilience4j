package project.resilience4j.algorithm.slidingwindow.timebased.request;

import lombok.Getter;

@Getter
public class ClientRequest {

    private final boolean isSuccess;
    private final long timestamp;

    public ClientRequest(
        boolean isSuccess,
        long timestamp
    ) {
        this.isSuccess = isSuccess;
        this.timestamp = timestamp;
    }
}
