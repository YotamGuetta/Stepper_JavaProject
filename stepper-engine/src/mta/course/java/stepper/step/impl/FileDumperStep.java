package mta.course.java.stepper.step.impl;

import mta.course.java.stepper.dd.impl.DataDefinitionRegistry;
import mta.course.java.stepper.flow.execution.context.StepExecutionContext;
import mta.course.java.stepper.step.api.AbstractStepDefinition;
import mta.course.java.stepper.step.api.DataDefinitionDeclarationImpl;
import mta.course.java.stepper.step.api.DataNecessity;
import mta.course.java.stepper.step.api.StepResult;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDumperStep extends AbstractStepDefinition {

    public FileDumperStep() {
        super("File Dumper", true);
        // step inputs
        addOutput(new DataDefinitionDeclarationImpl("CONTENT", DataNecessity.MANDATORY, "Content", DataDefinitionRegistry.STRING));
        addOutput(new DataDefinitionDeclarationImpl("FILE_NAME", DataNecessity.MANDATORY, "Target file path", DataDefinitionRegistry.STRING));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("RESULT", DataNecessity.NA, "File Creation Result", DataDefinitionRegistry.STRING));
    }

    private StepResult returnResult(StepExecutionContext context, StepResult result, String message){

        if(result == StepResult.FAILURE ||result == StepResult.WARNING) {
            addSummery(result + ": " + message);
            context.storeStepLogLine(this.name(), getSummery());
            if (result == StepResult.FAILURE) {
                context.storeDataValue("RESULT", getSummery());
                return result;
            }
        }
        return result;
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {

        String data = context.getDataValue("CONTENT", String.class);
        String location = context.getDataValue("FILE_NAME", String.class);
        Path path = Paths.get(location);

        context.storeStepLogLine(this.name(), "About to create file named "+path.getFileName());

        try {
            Files.createDirectories(path.getParent());
            try {
                Files.createFile(path);
                Files.write(path, data.getBytes());
            }
            catch (FileAlreadyExistsException e) {
                return returnResult(context,StepResult.FAILURE,"The file "+path.getFileName()+" already exists");
            }
        }
        catch(IOException e){
            return returnResult(context,StepResult.FAILURE,"The path "+location+" isn't valid");
        }
        // outputs
        context.storeDataValue("RESULT", "SUCCESS");

        if(data.isEmpty()){
            return returnResult(context,StepResult.WARNING,"The file content is empty");
        }
        return StepResult.SUCCESS;
    }
}
