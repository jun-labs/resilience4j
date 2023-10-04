package project.resilience4j.algorithm.test.unittest.countbased;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.resilience4j.algorithm.CircuitBreaker;
import project.resilience4j.algorithm.slidingwindow.countbased.CountBasedSlidingWindow;

@DisplayName("[UnitTest] CountBasedSlidingWindow 단위 테스트")
class CountBasedSlidingWindowUnitTest {

    private CircuitBreaker circuitBreaker;
    private CountBasedSlidingWindow countBasedSlidingWindow;

    @Test
    @DisplayName("임계치가 20%일 때, 이를 초과하면 서킷이 OPEN 된다.")
    void count_based_sliding_window_threshold_over_test() {
        circuitBreaker = new CircuitBreaker();
        countBasedSlidingWindow = new CountBasedSlidingWindow(10, 0.2, circuitBreaker);

        for (int index = 1; index <= 20; index++) {
            recordRequestStatus(index);
            boolean isSuccess = countBasedSlidingWindow.allowRequest();
            if (!isSuccess) {
                break;
            }
        }

        assertTrue(circuitBreaker.opened());
    }

    private void recordRequestStatus(int index) {
        if (index % 5 == 0) {
            countBasedSlidingWindow.recordRequest(false);
        } else {
            countBasedSlidingWindow.recordRequest(true);
        }
    }
}
