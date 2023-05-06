package mta.course.java.stepper.step.api;

import mta.course.java.stepper.flow.execution.context.StepExecutionContext;

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
