package stepper.flow.execution.context;

import javafx.util.Pair;
import stepper.alias.AliasMapping;
import stepper.dd.api.DataDefinition;
import stepper.flow.definition.api.FlowDefinition;
import stepper.step.api.DataCapsuleImpl;
import stepper.step.api.DataDefinitionDeclaration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StepExecutionContextImpl implements StepExecutionContext {

    private final Map<String, Object> dataValues;
    private final Map<String, DataDefinition> availableDataValues;
    private final StringBuilder logLines;
    private final StringBuilder summery;
    private final AliasMapping aliasMapping;
    private Map<Pair<String,String>, Pair<String, String>> customMapping;

    public StepExecutionContextImpl(FlowDefinition flowDefinition) {

        dataValues = new HashMap<>();
        availableDataValues = new HashMap<>();
        logLines = new StringBuilder();
        summery = new StringBuilder();
        aliasMapping = new AliasMapping();
        List<DataCapsuleImpl> allData = flowDefinition.getAllDataCapsules();
        for( DataCapsuleImpl data : allData){
                availableDataValues.put(data.getFinalName(),data.getDataDefinitionDeclaration().dataDefinition());
        }
    }
    @Override
    public <T> T getDataValue(String dataName, String step, Class<T> expectedDataType) {
        String dataFinalName = aliasMapping.getDataAliasName(step, dataName);
        Pair<String, String> targetData = new Pair<>(step, dataFinalName);
        if(customMapping.containsKey(targetData)){
            dataFinalName = customMapping.get(targetData).getValue();
        }
        // assuming that from the data name we can get to its data definition
        DataDefinition theExpectedDataDefinition = availableDataValues.get(dataFinalName);

        if (expectedDataType.isAssignableFrom(theExpectedDataDefinition.getType())) {
            Object aValue = dataValues.get(dataFinalName);
            // what happens if it does not exist ?

            return expectedDataType.cast(aValue);
        } else {
            logLines.append("Error: Cannot assign ").append(expectedDataType).append("to").append(theExpectedDataDefinition.getType().getSimpleName()).append("\n");

        }

        return null;
    }

    @Override
    public boolean storeDataValue(String dataName, String step, Object value) {
        String dataFinalName = aliasMapping.getDataAliasName(step, dataName);
        // assuming that from the data name we can get to its data definition
        DataDefinition theData = availableDataValues.get(dataFinalName);

        // we have the DD type, so we can make sure that its from the same type
        if (theData.getType().isAssignableFrom(value.getClass())) {
            dataValues.put(dataFinalName, value);
        } else {
            logLines.append("Error: Cannot assign ").append(theData).append("to").append(value.getClass().getSimpleName()).append("\n");
            return  true;
        }

        return false;
    }
    @Override
    public List<String> getAllOutputs(List<String> outputNames) {
        List<String> outputs = new ArrayList<>();

        for (String name : outputNames) {
            if (dataValues.containsKey(name))
                outputs.add(dataValues.get(name).toString());
            else
                outputs.add(name + " No Output");
        }

        return outputs;
    }
    public  String getOutput(String outputName){
        if (dataValues.containsKey(outputName))
            return dataValues.get(outputName).toString();
        else
            return outputName + " No Output";
    }
    @Override
    public List<String> getAllFormalOutputs(List<Pair<String, DataDefinitionDeclaration>> outputNames){
        List<String> outputs = new ArrayList<>();
        for (Pair<String, DataDefinitionDeclaration> output : outputNames) {
            if (dataValues.containsKey(output.getKey()))
                outputs.add(dataValues.get(output.getKey()).toString());
            else
                outputs.add(output.getKey() + " No Output");
        }

        return outputs;
    }

    @Override
    public void storeStepLogLine(String logLine) {
        logLines.append(logLine).append("\n");
    }

    @Override
    public String getStepLogLines() {
        return logLines.toString();
    }

    @Override
    public String getStepSummaryLine() {
        return summery.toString();
    }
    @Override
    public void assignCustomMapping(Map<Pair<String,String>, Pair<String, String>> customMapping){
        this.customMapping = customMapping;
    }

}
