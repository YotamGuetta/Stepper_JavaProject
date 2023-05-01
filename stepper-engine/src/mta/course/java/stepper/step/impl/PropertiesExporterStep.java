package mta.course.java.stepper.step.impl;

import mta.course.java.stepper.flow.execution.context.StepExecutionContext;
import mta.course.java.stepper.step.api.AbstractStepDefinition;
import mta.course.java.stepper.step.api.StepResult;

public class PropertiesExporterStep extends AbstractStepDefinition {

    public PropertiesExporterStep() {
        super("Hello World", true);

        // no inputs

        // no outputs
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        System.out.println("Hello world !");
        return StepResult.SUCCESS;
    }
}
