package project.resilience4j.algorithm.slidingwindow.timebased;

import java.time.Instant;
import java.util.LinkedList;
import lombok.extern.slf4j.Slf4j;
import project.resilience4j.algorithm.CircuitBreaker;
import project.resilience4j.algorithm.slidingwindow.RequestAllowable;
import project.resilience4j.algorithm.slidingwindow.timebased.request.ClientRequest;

@Slf4j
public class TimebasedSlidingWindow implements RequestAllowable {

    private final long windowDurationMillis;
    private final double failureThreshold;
    private final LinkedList<ClientRequest> window;
    private final CircuitBreaker circuitBreaker;
    private int failureCount;

    public TimebasedSlidingWindow(
        long windowDurationMillis,
        double failureThreshold,
        CircuitBreaker circuitBreaker
    ) {
        this.windowDurationMillis = windowDurationMillis;
        this.failureThreshold = failureThreshold;
        this.window = new LinkedList<>();
        this.circuitBreaker = circuitBreaker;
    }

    @Override
    public boolean allowRequest() {
        initRequestHistory();
        if (window.isEmpty()) {
            return true;
        }

        boolean isSuccess = ((double) failureCount / window.size() < failureThreshold);
        if (!isSuccess) {
            circuitBreaker.open();
            log.info("Circuit OPENED.");
        } else {
            if (circuitBreaker.opened()) {
                circuitBreaker.open();
            }
        }
        return isSuccess;
    }

    public void recordSuccess() {
        addRequestToWindow(true);
    }

    public void recordFailure() {
        addRequestToWindow(false);
    }

    private void addRequestToWindow(boolean isSuccess) {
        initRequestHistory();
        ClientRequest request = new ClientRequest(isSuccess, Instant.now().toEpochMilli());
        window.add(request);
        if (!isSuccess) {
            failureCount++;
        }
    }

    private void initRequestHistory() {
        long current = Instant.now().toEpochMilli();
        while (!window.isEmpty() && isBeforeFirstRequest(current)) {
            ClientRequest firstRequest = window.removeFirst();
            if (!firstRequest.isSuccess()) {
                failureCount--;
            }
        }
    }

    private boolean isBeforeFirstRequest(long current) {
        return current - window.getFirst().getTimestamp() > windowDurationMillis;
    }
}
