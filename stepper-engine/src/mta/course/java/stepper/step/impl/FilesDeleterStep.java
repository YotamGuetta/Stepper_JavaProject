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
        addOutput(new DataDefinitionDeclarationImpl("FILES_LIS", DataNecessity.MANDATORY, "Files to delete", DataDefinitionRegistry.LIST));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("DELETED_LIST", DataNecessity.NA, "Files failed to be deleted", DataDefinitionRegistry.LIST));
        addOutput(new DataDefinitionDeclarationImpl("DELETION_STATS", DataNecessity.NA, "Deletion summary results", DataDefinitionRegistry.MAPPING));
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {

        String directory = context.getDataValue("FOLDER_NAME", String.class);
        String filter = context.getDataValue("FILTER", String.class);

        int countFilesDeleted = 0;
        int countFilesFailed = 0;

        ListData<FileData> listOfFiles = context.getDataValue("FILES_LIS", ListData .class);
        ListData<FileData> failedToDelete = new ListData<>();

        context.storeStepLogLine(this.name(), "About to start delete "+listOfFiles.size()+" files");

        for(FileData file : listOfFiles){
            if(file.delete()){

                countFilesDeleted++;
            }
            else {
                failedToDelete.add(file);
                countFilesFailed++;
            }
        }

        context.storeStepLogLine(this.name(), "Failed to delete file "+countFilesFailed);

        MappingData<Number,Number> pair = new MappingData<>(countFilesDeleted, countFilesFailed);

        // outputs
        context.storeDataValue("DELETED_LIST", failedToDelete);
        context.storeDataValue("DELETION_STATS", pair);

        if(failedToDelete.isEmpty()) {
            addSummery("SUCCESS: All The files where deleted");
            return StepResult.SUCCESS;
        }
        else if(countFilesDeleted != 0){
            addSummery("WARNING: "+countFilesFailed+" files failed to delete");
            context.storeStepLogLine(this.name(), getSummery());
            return StepResult.WARNING;
        }
        addSummery("FAILURE: All files failed to delete");
        context.storeStepLogLine(this.name(), getSummery());
        return StepResult.FAILURE;
    }

}
