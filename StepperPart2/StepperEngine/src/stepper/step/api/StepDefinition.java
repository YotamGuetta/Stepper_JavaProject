package stepper.step.api;

import stepper.flow.execution.context.StepExecutionContext;
import stepper.step.api.DataDefinitionDeclaration;

import java.util.List;

public interface StepDefinition {
    String name();
    boolean isReadonly();
    List<DataDefinitionDeclaration> inputs();
    List<DataDefinitionDeclaration> outputs();
    StepResult invoke(StepExecutionContext context, String stepFinaleName);
    String getSummery();
    long getRunTime();
}
