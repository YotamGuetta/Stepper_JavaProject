package mta.course.java.stepper.step.impl;

import mta.course.java.stepper.dd.impl.DataDefinitionRegistry;
import mta.course.java.stepper.dd.impl.file.FileData;
import mta.course.java.stepper.dd.impl.list.ListData;
import mta.course.java.stepper.dd.impl.mapping.MappingData;
import mta.course.java.stepper.flow.execution.context.StepExecutionContext;
import mta.course.java.stepper.step.api.AbstractStepDefinition;
import mta.course.java.stepper.step.api.DataDefinitionDeclarationImpl;
import mta.course.java.stepper.step.api.DataNecessity;
import mta.course.java.stepper.step.api.StepResult;


public class FilesDeleterStep extends AbstractStepDefinition {

    public FilesDeleterStep() {
        super("Files Deleter", false);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("FILES_LIST",super.name(), DataNecessity.MANDATORY, "Files to delete", DataDefinitionRegistry.LIST));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("DELETED_LIST",super.name(), DataNecessity.NA, "Files failed to be deleted", DataDefinitionRegistry.LIST));
        addOutput(new DataDefinitionDeclarationImpl("DELETION_STATS",super.name(), DataNecessity.NA, "Deletion summary results", DataDefinitionRegistry.MAPPING));
    }

    @Override
    public StepResult invoke(StepExecutionContext context, String stepFinaleName) {

        int countFilesDeleted = 0;
        int countFilesFailed = 0;

        ListData<FileData> listOfFiles = context.getDataValue("FILES_LIST",stepFinaleName, ListData .class);
        ListData<FileData> failedToDelete = new ListData<>();

        context.storeStepLogLine("About to start delete "+listOfFiles.size()+" files");

        for(FileData file : listOfFiles){
            if(file.delete()){

                countFilesDeleted++;
            }
            else {
                failedToDelete.add(file);
                context.storeStepLogLine("Failed to delete file "+file.toString());
                countFilesFailed++;
            }
        }



        MappingData<Number,Number> pair = new MappingData<>(countFilesDeleted, countFilesFailed);

        // outputs
        context.storeDataValue("DELETED_LIST", stepFinaleName, failedToDelete);
        context.storeDataValue("DELETION_STATS", stepFinaleName, pair);

        if(failedToDelete.isEmpty()) {
            addSummery("SUCCESS: All The files where deleted");
            return StepResult.SUCCESS;
        }
        else if(countFilesDeleted != 0){
            addSummery("WARNING: "+countFilesFailed+" files failed to delete");
            context.storeStepLogLine(getSummery());
            return StepResult.WARNING;
        }
        addSummery("FAILURE: All files failed to delete");
        context.storeStepLogLine(getSummery());
        return StepResult.FAILURE;
    }

}
