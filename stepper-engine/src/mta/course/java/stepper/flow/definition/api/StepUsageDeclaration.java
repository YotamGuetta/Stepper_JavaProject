package mta.course.java.stepper.flow.definition.api;

import mta.course.java.stepper.step.api.StepDefinition;

public interface StepUsageDeclaration {
    String getFinalStepName();
    StepDefinition getStepDefinition();
    boolean skipIfFail();
    long getStepRunTime();
    void setStepRunTime(long runTime);
    String getStepResult();
    void setStepResult(String stepResult);

}
