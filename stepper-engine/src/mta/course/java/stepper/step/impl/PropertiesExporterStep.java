package mta.course.java.stepper.step.impl;

import mta.course.java.stepper.dd.impl.DataDefinitionRegistry;
import mta.course.java.stepper.dd.impl.relation.RelationData;
import mta.course.java.stepper.flow.execution.context.StepExecutionContext;
import mta.course.java.stepper.step.api.AbstractStepDefinition;
import mta.course.java.stepper.step.api.DataDefinitionDeclarationImpl;
import mta.course.java.stepper.step.api.DataNecessity;
import mta.course.java.stepper.step.api.StepResult;

import java.util.ArrayList;
import java.util.List;

public class PropertiesExporterStep extends AbstractStepDefinition {

    public PropertiesExporterStep() {
        super("Properties Exporter", true);
        // step inputs
        addOutput(new DataDefinitionDeclarationImpl("SOURCE", DataNecessity.MANDATORY, "Source data", DataDefinitionRegistry.RELATION));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("RESULT", DataNecessity.NA, "Properties export result", DataDefinitionRegistry.STRING));

    }

    private String convertStringListToProperties( List<String> keys, List<String> values){
        StringBuilder result = new StringBuilder();
        for(int i=0; i<keys.size(); i++){
            result.append("row-"+i+".").append(keys.get(i)).append("=").append(values.get(i));
        }
        result.append("\n");

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
    public StepResult invoke(StepExecutionContext context) {
        RelationData source = context.getDataValue("SOURCE", RelationData.class);

        context.storeStepLogLine(this.name(), "About to process " + source.size() + " lines of data");

        StringBuilder result = new StringBuilder();
        List<String> columns = source.getColumns();

        for (int i = 0; i < source.size(); i++) {
            result.append(convertStringListToProperties(columns, source.getRowDataByColumnsOrder(i)));
        }

        context.storeStepLogLine(this.name(), "Extracted total of " + countWords(result.toString()));

        // outputs
        context.storeDataValue("RESULT", result);

        if (source.size() == 0) {
            addSummery("WARNING: The relation is empty");
            context.storeStepLogLine(this.name(), getSummery());
            return StepResult.WARNING;
        }

        return StepResult.SUCCESS;
    }
}
