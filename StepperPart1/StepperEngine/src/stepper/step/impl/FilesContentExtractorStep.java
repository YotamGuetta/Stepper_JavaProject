package stepper.step.impl;

import stepper.dd.impl.DataDefinitionRegistry;
import stepper.dd.impl.file.FileData;
import stepper.dd.impl.list.ListData;
import stepper.dd.impl.relation.RelationData;
import stepper.flow.execution.context.StepExecutionContext;
import stepper.step.api.AbstractStepDefinition;
import stepper.step.api.DataDefinitionDeclarationImpl;
import stepper.step.api.DataNecessity;
import stepper.step.api.StepResult;

import java.util.ArrayList;
import java.util.List;

public class FilesContentExtractorStep extends AbstractStepDefinition {

    public FilesContentExtractorStep() {
        super("Files Content Extractor", true);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("FILES_LIST",super.name(), DataNecessity.MANDATORY, "Files to extract", DataDefinitionRegistry.LIST));
        addInput(new DataDefinitionDeclarationImpl("LINE",super.name(), DataNecessity.MANDATORY, "Line number to extract", DataDefinitionRegistry.NUMBER));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("DATA",super.name(), DataNecessity.NA, "File not found", DataDefinitionRegistry.RELATION));

    }

    @Override
    public StepResult invoke(StepExecutionContext context, String stepFinaleName) {
        int countFiles = 1;
        String line;

        List<String> relationData = new ArrayList<>(3);
        List<String> columns = new ArrayList<>(3);

        columns.add("Index");
        columns.add("Original File Name");
        columns.add("Data In File Line");
        RelationData filesNotFound = new RelationData(columns);

        ListData<FileData> listOfFiles = context.getDataValue("FILES_LIST",stepFinaleName, ListData.class);
        int lineNumber = context.getDataValue("LINE",stepFinaleName, Integer.class);

        for (FileData file : listOfFiles) {
            try {
                line = file.getLineFromFile(lineNumber);
                if (line.isEmpty()) {
                    line = "Not such line";
                    context.storeStepLogLine("Problem extracting line number " + lineNumber + " from file " + file);
                }

            } catch (Exception e) {
                line = " File not found";
                context.storeStepLogLine("Problem extracting line number " + lineNumber + " from file " + file);
            }

            relationData.set(0, countFiles + "");
            relationData.set(1, file.toString());
            relationData.set(2, line);

            filesNotFound.addData(relationData);
            countFiles++;
        }

        // outputs
        context.storeDataValue("DATA", stepFinaleName, filesNotFound);

        addSummery("SUCCESS: All Files are renamed successfully");
        if (listOfFiles.isEmpty()) {
            addSummery("SUCCESS: the list is empty");
            context.storeStepLogLine("the list is empty");
        }

        return StepResult.SUCCESS;
    }
}
