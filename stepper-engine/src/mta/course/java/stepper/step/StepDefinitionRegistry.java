package mta.course.java.stepper.step;

import mta.course.java.stepper.flow.execution.context.StepExecutionContext;
import mta.course.java.stepper.step.api.DataDefinitionDeclaration;
import mta.course.java.stepper.step.api.StepDefinition;
import mta.course.java.stepper.step.api.StepResult;
import mta.course.java.stepper.step.impl.*;

import java.util.List;

public enum StepDefinitionRegistry implements StepDefinition {
    HELLO_WORLD(new HelloWorldStep()),
    PERSON_DETAILS(new PersonDetailsStep()),
    SPEND_SOME_TIME(new SpendSomeTimeStep()),
    COLLECT_FILES_IN_FOLDER(new CollectFilesInFolderStep()),
    FILES_DELETER(new FilesDeleterStep()),
    FILES_RENAMER_STEP(new FilesRenamerStep()),
    FILES_CONTENT_EXTRACTOR(new FilesContentExtractorStep()),
    CSV_EXPORTER(new CSVExporterStep()),
    PROPERTIES_EXPORTER(new PropertiesExporterStep()),
    FILE_DUMPER(new FileDumperStep());


    StepDefinitionRegistry(StepDefinition stepDefinition) {
        this.stepDefinition = stepDefinition;
    }

    private final StepDefinition stepDefinition;

    public StepDefinition getStepDefinition() {
        return stepDefinition;
    }


    public String getName() {
        return stepDefinition.name();
    }

    public boolean isReadonly() {
        return stepDefinition.isReadonly();
    }

    public List<DataDefinitionDeclaration> inputs() {
        return stepDefinition.inputs();
    }

    public List<DataDefinitionDeclaration> outputs() {
        return stepDefinition.outputs();
    }

    public StepResult invoke(StepExecutionContext context) {
        return stepDefinition.invoke(context);
    }

    public String getSummery() {
        return stepDefinition.getSummery();
    }
    public long getRunTime(){
        return stepDefinition.getRunTime();
    }
}
