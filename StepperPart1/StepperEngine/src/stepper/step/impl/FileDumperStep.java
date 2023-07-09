package stepper.step.impl;

import stepper.dd.impl.DataDefinitionRegistry;
import stepper.flow.execution.context.StepExecutionContext;
import stepper.step.api.AbstractStepDefinition;
import stepper.step.api.DataDefinitionDeclarationImpl;
import stepper.step.api.DataNecessity;
import stepper.step.api.StepResult;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDumperStep extends AbstractStepDefinition {

    public FileDumperStep() {
        super("File Dumper", true);
        // step inputs
        addInput(new DataDefinitionDeclarationImpl("CONTENT", super.name(), DataNecessity.MANDATORY, "Content", DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl("FILE_NAME", super.name(), DataNecessity.MANDATORY, "Target file path", DataDefinitionRegistry.STRING));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("RESULT", super.name(), DataNecessity.NA, "File Creation Result", DataDefinitionRegistry.STRING));
    }

    private StepResult returnResult(StepExecutionContext context, StepResult result, String message){

        if(result == StepResult.FAILURE ||result == StepResult.WARNING) {
            addSummery(result + ": " + message);
            context.storeStepLogLine(getSummery());
            if (result == StepResult.FAILURE) {
                context.storeDataValue("RESULT", super.name(), getSummery());
                return result;
            }
        }
        return result;
    }

    @Override
    public StepResult invoke(StepExecutionContext context, String stepFinaleName) {

        String data = context.getDataValue("CONTENT",stepFinaleName, String.class);
        String location = context.getDataValue("FILE_NAME",stepFinaleName, String.class);
        if(location == null)
            return returnResult(context,StepResult.WARNING,"The file FILE_NAME is empty");
        Path path = Paths.get(location);

        context.storeStepLogLine("About to create file named "+path.getFileName());

        try {
            Files.createDirectories(path.getParent());
            try {
                Files.createFile(path);
                if(data == null || data.isEmpty()){
                    return returnResult(context,StepResult.WARNING,"The file content is empty");
                }
                else
                    Files.write(path, data.getBytes());
            }
            catch (FileAlreadyExistsException e) {
                return returnResult(context,StepResult.FAILURE,"The file "+path.getFileName()+" already exists");
            }
        }
        catch(IOException e){
            return returnResult(context,StepResult.FAILURE,"The path "+location+" isn't valid or a file");
        }
        // outputs
        context.storeDataValue("RESULT", stepFinaleName, "SUCCESS");


        return StepResult.SUCCESS;
    }
}
