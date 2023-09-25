package project.resilience4j.algorithm;

public class CircuitBreaker {

    private boolean isOpened;

    public void open() {
        this.isOpened = true;
    }

    public void close() {
        this.isOpened = false;
    }

    public boolean opened() {
        return isOpened;
    }
}
