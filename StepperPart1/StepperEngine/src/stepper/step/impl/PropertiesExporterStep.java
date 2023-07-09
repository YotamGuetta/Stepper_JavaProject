package stepper.step.impl;

import stepper.dd.impl.DataDefinitionRegistry;
import stepper.dd.impl.relation.RelationData;
import stepper.flow.execution.context.StepExecutionContext;
import stepper.step.api.AbstractStepDefinition;
import stepper.step.api.DataDefinitionDeclarationImpl;
import stepper.step.api.DataNecessity;
import stepper.step.api.StepResult;

import java.util.List;

public class PropertiesExporterStep extends AbstractStepDefinition {

    public PropertiesExporterStep() {
        super("Properties Exporter", true);
        // step inputs
        addInput(new DataDefinitionDeclarationImpl("SOURCE",super.name(), DataNecessity.MANDATORY, "Source data", DataDefinitionRegistry.RELATION));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("RESULT",super.name(), DataNecessity.NA, "Properties export result", DataDefinitionRegistry.STRING));

    }

    private String convertStringListToProperties( List<String> keys, List<String> values){
        StringBuilder result = new StringBuilder();
        for(int i=0; i<keys.size(); i++){
            result.append("row-").append(i).append(".").append(keys.get(i)).append("=").append(values.get(i)).append("\n");
        }

        return result.toString();
    }
    private static int countWords(String input) {
        if (input == null || input.isEmpty()) {
            return 0;
        }
        String[] words = input.split("\\s+");
        return words.length;
    }

    @Override
    public StepResult invoke(StepExecutionContext context, String stepFinaleName) {
        RelationData source = context.getDataValue("SOURCE", stepFinaleName , RelationData.class);

        context.storeStepLogLine("About to process " + source.size() + " lines of data");

        StringBuilder result = new StringBuilder();
        List<String> columns = source.getColumns();

        for (int i = 0; i < source.size(); i++) {
            result.append(convertStringListToProperties(columns, source.getRowDataByColumnsOrder(i)));
        }

        context.storeStepLogLine("Extracted total of " + countWords(result.toString()));

        // outputs
        context.storeDataValue("RESULT", stepFinaleName, result.toString());

        if (source.size() == 0) {
            addSummery("WARNING: The relation is empty");
            context.storeStepLogLine(getSummery());
            return StepResult.WARNING;
        }

        return StepResult.SUCCESS;
    }
}
