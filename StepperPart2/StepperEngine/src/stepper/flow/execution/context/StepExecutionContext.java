package stepper.flow.execution.context;

import javafx.util.Pair;
import stepper.step.api.DataDefinitionDeclaration;

import java.util.List;
import java.util.Map;

public interface StepExecutionContext {
    public <T> T getDataValue(String dataName, String step, Class<T> expectedDataType);
    boolean storeDataValue(String dataName, String step, Object value);
    List<String> getAllOutputs(List<String> outputNames);
    List<String> getAllFormalOutputs(List<Pair<String, DataDefinitionDeclaration>> outputNames);
    void storeStepLogLine(String logLine, String step);
    String getStepLogLines(String stepName);
    String getStepSummaryLine();
    String getOutput(String outputName);
    void assignCustomMapping(Map<Pair<String,String>, Pair<String, String>> customMapping);
    Object GetDataValueAsObject(String dataName,String step);
}
