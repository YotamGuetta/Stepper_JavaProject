package stepper.step.impl;

import stepper.dd.impl.DataDefinitionRegistry;
import stepper.dd.impl.relation.RelationData;
import stepper.flow.execution.context.StepExecutionContext;
import stepper.step.api.AbstractStepDefinition;
import stepper.step.api.DataDefinitionDeclarationImpl;
import stepper.step.api.DataNecessity;
import stepper.step.api.StepResult;

import java.util.ArrayList;
import java.util.List;

public class CSVExporterStep extends AbstractStepDefinition {

    public CSVExporterStep() {
        super("CSV Exporter", true);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("SOURCE",super.name(), DataNecessity.MANDATORY, "Source data", DataDefinitionRegistry.RELATION));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("RESULT",super.name(), DataNecessity.NA, "CSV export result", DataDefinitionRegistry.STRING));
    }

    private String convertStringListToCSV( List<String> stringsToCon){
        StringBuilder result = new StringBuilder();
        for(String name : stringsToCon){
            result.append(name).append(", ");
        }
        result.deleteCharAt(result.length()-1);     //
        result.deleteCharAt(result.length()-1);     // Remove the ", " at the end
        result.append("\n");

        return result.toString();
    }
    @Override
    public StepResult invoke(StepExecutionContext context, String stepFinaleName) {
        addRunTime(System.currentTimeMillis());
        StringBuilder result;
        List<String> row = new ArrayList<>();
        RelationData source = context.getDataValue("SOURCE",super.name(), RelationData.class);

        context.storeStepLogLine("About to process "+source.size()+" lines of data");

        result = new StringBuilder(convertStringListToCSV(source.getColumns()));

        for(int i=0; i<source.size(); i++){
            result.append(convertStringListToCSV(source.getRowDataByColumnsOrder(i)));
        }

        // outputs
        context.storeDataValue("RESULT", stepFinaleName, result.toString());

        if(source.size() == 0){
            addSummery("WARNING: The relation is empty");
            context.storeStepLogLine(getSummery());
            addRunTime(System.currentTimeMillis() - getRunTime());
            return  StepResult.WARNING;
        }

        return StepResult.SUCCESS;
    }
}
