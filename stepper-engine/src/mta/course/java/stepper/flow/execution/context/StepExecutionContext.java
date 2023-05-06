package mta.course.java.stepper.flow.execution.context;

import javafx.util.Pair;
import mta.course.java.stepper.step.api.DataDefinitionDeclaration;

import java.util.List;
import java.util.Map;

public interface StepExecutionContext {
    public <T> T getDataValue(String dataName, String step, Class<T> expectedDataType);
    boolean storeDataValue(String dataName, String step, Object value);
    List<String> getAllOutputs(List<String> outputNames);
    List<String> getAllFormalOutputs(List<Pair<String, DataDefinitionDeclaration>> outputNames);
    void storeStepLogLine(String logLine);
    String getStepLogLines();
    String getStepSummaryLine();
    String getOutput(String outputName);
    void assignCustomMapping(Map<Pair<String,String>, Pair<String, String>> customMapping);
    // some more utility methods:
    // allow step to store log lines
    // allow steps to declare their summary line
}
