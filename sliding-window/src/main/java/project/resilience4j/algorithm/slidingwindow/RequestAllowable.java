package project.resilience4j.algorithm.slidingwindow;

public interface RequestAllowable {

    boolean allowRequest();
}
