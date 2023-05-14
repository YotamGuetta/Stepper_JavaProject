package stepper.flow.definition.api;

import stepper.step.api.StepDefinition;

public class StepUsageDeclarationImpl implements StepUsageDeclaration {
    private final StepDefinition stepDefinition;
    private final boolean skipIfFail;
    private final String stepName;
    private  long stepRunTime;
    private  String result;

    public StepUsageDeclarationImpl(StepDefinition stepDefinition) {
        this(stepDefinition, false, stepDefinition.name());
    }

    public StepUsageDeclarationImpl(StepDefinition stepDefinition, String name) {
        this(stepDefinition, false, name);

    }

    public StepUsageDeclarationImpl(StepDefinition stepDefinition, boolean skipIfFail, String stepName) {
        this.stepDefinition = stepDefinition;
        this.skipIfFail = skipIfFail;
        this.stepName = stepName;
    }

    @Override
    public String getFinalStepName() {
        return stepName;
    }

    @Override
    public StepDefinition getStepDefinition() {
        return stepDefinition;
    }

    @Override
    public boolean skipIfFail() {
        return skipIfFail;
    }
    @Override
    public  long getStepRunTime(){
        return  stepRunTime;
    }
    @Override
    public void setStepRunTime(long runTime){
        stepRunTime = runTime;
    }
    @Override
    public String getStepResult(){
        return result;
    }
    @Override
    public void setStepResult(String stepResult){
        result = stepResult;
    }

}
