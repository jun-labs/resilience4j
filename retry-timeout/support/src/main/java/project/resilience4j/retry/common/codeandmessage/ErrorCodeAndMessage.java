package project.resilience4j.retry.common.codeandmessage;

public interface ErrorCodeAndMessage extends CodeAndMessage{

    String getKrErrorMessage();

    String getEnErrorMessage();
}
