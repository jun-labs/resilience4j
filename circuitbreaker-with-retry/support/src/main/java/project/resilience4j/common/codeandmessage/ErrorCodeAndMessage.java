package project.resilience4j.common.codeandmessage;

public interface ErrorCodeAndMessage extends CodeAndMessage{

    String getKrErrorMessage();

    String getEnErrorMessage();
}
