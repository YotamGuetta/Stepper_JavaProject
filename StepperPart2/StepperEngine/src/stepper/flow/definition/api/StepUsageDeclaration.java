package stepper.flow.definition.api;

import stepper.step.api.StepDefinition;

public interface StepUsageDeclaration {
    String getFinalStepName();
    StepDefinition getStepDefinition();
    boolean skipIfFail();
    long getStepRunTime();
    void setStepRunTime(long runTime);
    String getStepResult();
    void setStepResult(String stepResult);

}
