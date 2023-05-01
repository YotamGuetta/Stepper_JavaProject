package mta.course.java.stepper.step.impl;

import mta.course.java.stepper.dd.impl.DataDefinitionRegistry;
import mta.course.java.stepper.dd.impl.file.FileData;
import mta.course.java.stepper.dd.impl.list.ListData;
import mta.course.java.stepper.dd.impl.relation.RelationData;
import mta.course.java.stepper.flow.execution.context.StepExecutionContext;
import mta.course.java.stepper.step.api.AbstractStepDefinition;
import mta.course.java.stepper.step.api.DataDefinitionDeclarationImpl;
import mta.course.java.stepper.step.api.DataNecessity;
import mta.course.java.stepper.step.api.StepResult;

import java.util.ArrayList;
import java.util.List;

public class FilesContentExtractorStep extends AbstractStepDefinition {

    public FilesContentExtractorStep() {
        super("Files Content Extractor", true);

        // step inputs
        addOutput(new DataDefinitionDeclarationImpl("FILES_LIST", DataNecessity.MANDATORY, "Files to extract", DataDefinitionRegistry.LIST));
        addOutput(new DataDefinitionDeclarationImpl("LINE", DataNecessity.MANDATORY, "Line number to extract", DataDefinitionRegistry.NUMBER));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("DATA", DataNecessity.NA, "File not found", DataDefinitionRegistry.RELATION));

    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        int countFiles = 1;
        String line;

        List<String> relationData = new ArrayList<>(3);
        List<String> columns = new ArrayList<>(3);

        columns.add("Index");
        columns.add("Original File Name");
        columns.add("Data In File Line");
        RelationData filesNotFound = new RelationData(columns);

        ListData<FileData> listOfFiles = context.getDataValue("FILES_LIST", ListData.class);
        int lineNumber = context.getDataValue("LINE", Integer.class);

        for (FileData file : listOfFiles) {
            try {
                line = file.getLineFromFile(lineNumber);
                if (line.isEmpty()) {
                    line = "Not such line";
                    context.storeStepLogLine(this.name(), "Problem extracting line number " + lineNumber + " from file " + file);
                }

            } catch (Exception e) {
                line = " File not found";
                context.storeStepLogLine(this.name(), "Problem extracting line number " + lineNumber + " from file " + file);
            }

            relationData.set(0, countFiles + "");
            relationData.set(1, file.toString());
            relationData.set(2, line);

            filesNotFound.addData(relationData);
            countFiles++;
        }

        // outputs
        context.storeDataValue("DATA", filesNotFound);

        addSummery("SUCCESS: All Files are renamed successfully");
        if (listOfFiles.isEmpty()) {
            addSummery("SUCCESS: the list is empty");
            context.storeStepLogLine(this.name(), "the list is empty");
        }

        return StepResult.SUCCESS;
    }
}
