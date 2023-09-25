package project.resilience4j.algorithm.test.unittest.timebased;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.resilience4j.algorithm.CircuitBreaker;
import project.resilience4j.algorithm.slidingwindow.timebased.TimebasedSlidingWindow;

@DisplayName("[UnitTest] TimeBasedSlidingWindow 단위 테스트")
class TimeBasedSlidingWindowUnitTest {

    private CircuitBreaker circuitBreaker;
    private TimebasedSlidingWindow timebasedSlidingWindow;

    @Test
    @DisplayName("임계치가 20%일 때, 이를 초과하면 서킷이 OPEN된다.")
    void time_based_sliding_window_threshold_over_test() {
        circuitBreaker = new CircuitBreaker();
        timebasedSlidingWindow = new TimebasedSlidingWindow(1000, 0.2, circuitBreaker);

        putSuccessRequest();
        putFailureRequest();

        timebasedSlidingWindow.allowRequest();

        assertTrue(circuitBreaker.opened());
    }

    @Test
    @DisplayName("임계치를 초과해도 처음 요청이 1초 전이라면 서킷이 OPEN 되지 않는다.")
    void time_based_sliding_window_threshold_not_over_test() throws Exception {
        circuitBreaker = new CircuitBreaker();
        timebasedSlidingWindow = new TimebasedSlidingWindow(1000, 0.2, circuitBreaker);

        putFailureRequest();
        Thread.sleep(1500L);

        timebasedSlidingWindow.allowRequest();

        assertFalse(circuitBreaker.opened());
    }

    private void putSuccessRequest() {
        for (int index = 1; index <= 8; index++) {
            timebasedSlidingWindow.recordSuccess();
        }
    }

    private void putFailureRequest() {
        for (int index = 1; index <= 2; index++) {
            timebasedSlidingWindow.recordFailure();
        }
    }
}
