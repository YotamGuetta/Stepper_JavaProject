package stepper.step.impl;

import stepper.dd.impl.DataDefinitionRegistry;
import stepper.flow.execution.context.StepExecutionContext;
import stepper.step.api.AbstractStepDefinition;
import stepper.step.api.DataDefinitionDeclarationImpl;
import stepper.step.api.DataNecessity;
import stepper.step.api.StepResult;

import java.io.IOException;

public class CommandLineStep extends AbstractStepDefinition {
    public CommandLineStep(){
        super("Command Line", false);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("COMMAND",super.name(), DataNecessity.MANDATORY, "Command", DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl("ARGUMENT",super.name(), DataNecessity.OPTIONAL, "Command argument", DataDefinitionRegistry.STRING));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("RESULT",super.name(), DataNecessity.NA, "Command output", DataDefinitionRegistry.STRING));

    }

    @Override
    public StepResult invoke(StepExecutionContext context, String stepFinaleName) {
        String command = context.getDataValue("COMMAND",stepFinaleName, String.class);
        String argument = context.getDataValue("ARGUMENT",stepFinaleName, String.class);

        context.storeStepLogLine("About to invoke "+command+" "+argument, stepFinaleName);

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command, argument);

            Process process = processBuilder.start();
            int exitCode = process.waitFor();

        } catch (Exception e) {

        }

        return StepResult.SUCCESS;
    }
}
