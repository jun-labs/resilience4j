package project.resilience4j.algorithm.slidingwindow.countbased;

import java.util.LinkedList;
import lombok.extern.slf4j.Slf4j;
import project.resilience4j.algorithm.CircuitBreaker;
import project.resilience4j.algorithm.slidingwindow.RequestAllowable;

@Slf4j
public class CountBasedSlidingWindow implements RequestAllowable {

    private final int windowSize;
    private final double failureThreshold;
    private int failureCount;
    private final LinkedList<Boolean> window;
    private final CircuitBreaker circuitBreaker;

    public CountBasedSlidingWindow(
        int windowSize,
        double failureThreshold,
        CircuitBreaker circuitBreaker
    ) {
        this.windowSize = windowSize;
        this.failureThreshold = failureThreshold;
        this.window = new LinkedList<>();
        this.circuitBreaker = circuitBreaker;
    }

    @Override
    public boolean allowRequest() {
        synchronized (window) {
            int currentWindowSize = window.size();
            if (currentWindowSize == 0) {
                return true;
            }

            boolean allowRequest = (double) failureCount / currentWindowSize < failureThreshold;
            if (!allowRequest) {
                circuitBreaker.open();
                log.info("Circuit OPENED.");
            } else {
                if (circuitBreaker.opened()) {
                    circuitBreaker.close();
                }
            }
            return allowRequest;
        }
    }

    public void recordSuccess() {
        synchronized (window) {
            addRequestToWindow(true);
        }
    }

    public void recordFailure() {
        synchronized (window) {
            addRequestToWindow(false);
        }
    }

    private void addRequestToWindow(boolean isSuccess) {
        if (window.size() >= windowSize) {
            Boolean firstRequest = window.removeFirst();
            if (Boolean.FALSE.equals(firstRequest)) {
                failureCount--;
            }
        }
        window.addLast(isSuccess);
        if (!isSuccess) {
            failureCount++;
        }
    }
}
