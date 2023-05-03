package mta.course.java.stepper.flow.execution.context;

import mta.course.java.stepper.dd.api.DataDefinition;
import mta.course.java.stepper.dd.impl.DataDefinitionRegistry;
import mta.course.java.stepper.flow.definition.api.StepUsageDeclaration;
import mta.course.java.stepper.step.StepDefinitionRegistry;
import mta.course.java.stepper.step.api.DataDefinitionDeclaration;
import mta.course.java.stepper.step.api.StepDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StepExecutionContextImpl implements StepExecutionContext {

    private final Map<String, Object> dataValues;
    private final Map<String, DataDefinition> availableDataValues;
    private final StringBuilder logLines;
    private final StringBuilder summery;

    public StepExecutionContextImpl(List<StepUsageDeclaration> steps) {

        dataValues = new HashMap<>();
        availableDataValues = new HashMap<>();
        logLines = new StringBuilder();
        summery = new StringBuilder();

        for( StepUsageDeclaration step : steps){
            for(DataDefinitionDeclaration input: step.getStepDefinition().inputs())
                availableDataValues.put(input.getName(),input.dataDefinition());

            for(DataDefinitionDeclaration output: step.getStepDefinition().outputs())
                availableDataValues.put(output.getName(),output.dataDefinition());

            //logLines.put(step.getStepDefinition().name(), new ArrayList<>());
        }
    }
    @Override
    public <T> T getDataValue(String dataName, Class<T> expectedDataType) {
        // assuming that from the data name we can get to its data definition
        DataDefinition theExpectedDataDefinition = availableDataValues.get(dataName);

        if (expectedDataType.isAssignableFrom(theExpectedDataDefinition.getType())) {
            Object aValue = dataValues.get(dataName);
            // what happens if it does not exist ?

            return expectedDataType.cast(aValue);
        } else {


            // error handling of some sort...
        }

        return null;
    }

    @Override
    public boolean storeDataValue(String dataName, Object value) {

        // assuming that from the data name we can get to its data definition
        DataDefinition theData = availableDataValues.get(dataName);

        // we have the DD type, so we can make sure that its from the same type
        if (theData.getType().isAssignableFrom(value.getClass())) {
            dataValues.put(dataName, value);
        } else {
            // error handling of some sort...
        }

        return false;
    }
    @Override
    public List<String> getAllOutputs(List<String> outputNames) throws NullPointerException {
        List<String> outputs = new ArrayList<>();

        for (String name : outputNames) {

            outputs.add(dataValues.get(name).toString());
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


}
