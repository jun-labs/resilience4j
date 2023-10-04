package project.resilience4j.algorithm.slidingwindow.timebased;

import lombok.extern.slf4j.Slf4j;
import project.resilience4j.algorithm.CircuitBreaker;
import project.resilience4j.algorithm.slidingwindow.RequestAllowable;
import project.resilience4j.algorithm.slidingwindow.timebased.request.ClientRequest;

import java.time.Instant;
import java.util.LinkedList;

@Slf4j
public class TimeBasedSlidingWindow implements RequestAllowable {

    private final long windowDurationMillis;
    private final double failureThreshold;
    private final LinkedList<ClientRequest> window;
    private final CircuitBreaker circuitBreaker;
    private int failureCount;

    public TimeBasedSlidingWindow(
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
            recordRequest(true);
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

    @Override
    public void recordRequest(boolean isSuccess) {
        addRequestToWindow(isSuccess);
    }

    private void addRequestToWindow(boolean isSuccess) {
        initRequestHistory();
        ClientRequest request = new ClientRequest(isSuccess, getMillis(Instant.now()));
        window.add(request);
        if (!isSuccess) {
            failureCount++;
        }
    }

    private void initRequestHistory() {
        long current = getMillis(Instant.now());
        while (!window.isEmpty() && isBeforeFirstRequest(current)) {
            ClientRequest firstRequest = window.removeFirst();
            if (!firstRequest.isSuccess()) {
                failureCount--;
            }
        }
    }

    private long getMillis(Instant time) {
        return time.toEpochMilli();
    }

    private boolean isBeforeFirstRequest(long current) {
        return current - window.getFirst().getTimestamp() > windowDurationMillis;
    }
}
