package mta.course.java.stepper.flow.execution.context;

import java.util.List;

public interface StepExecutionContext {
    <T> T getDataValue(String dataName, Class<T> expectedDataType);
    boolean storeDataValue(String dataName, Object value);
    List<String> getAllOutputs(List<String> outputNames);
    void storeStepLogLine(String logLine);
    String getStepLogLines();
    String getStepSummaryLine();
    // some more utility methods:
    // allow step to store log lines
    // allow steps to declare their summary line
}
