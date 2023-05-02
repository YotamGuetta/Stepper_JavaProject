package mta.course.java.stepper.flow.execution.runner;

import mta.course.java.stepper.dd.impl.DataDefinitionRegistry;
import mta.course.java.stepper.flow.definition.api.StepUsageDeclaration;
import mta.course.java.stepper.flow.execution.FlowExecution;
import mta.course.java.stepper.flow.execution.FlowExecutionResult;
import mta.course.java.stepper.flow.execution.context.StepExecutionContext;
import mta.course.java.stepper.flow.execution.context.StepExecutionContextImpl;
import mta.course.java.stepper.step.api.DataDefinitionDeclaration;
import mta.course.java.stepper.step.api.DataDefinitionDeclarationImpl;
import mta.course.java.stepper.step.api.DataNecessity;
import mta.course.java.stepper.step.api.StepResult;

import java.util.List;
import java.util.Scanner;

public class FLowExecutor {

    public void executeFlow(FlowExecution flowExecution) {

        System.out.println("Starting execution of flow " + flowExecution.getFlowDefinition().getName() + " [ID: " + flowExecution.getUniqueId() + "]");

        StepExecutionContext context = new StepExecutionContextImpl(flowExecution.getFlowDefinition().getFlowSteps()); // actual object goes here...

        FlowExecutionResult result = FlowExecutionResult.SUCCESS;

        // populate context with all free inputs (mandatory & optional) that were given from the user
        // (typically stored on top of the flow execution object)

        for (String key : flowExecution.getFreeInputs().keySet()) {
            context.storeDataValue(key, flowExecution.getFreeInputs().get(key));
        }

        // start actual execution
        for (int i = 0; i < flowExecution.getFlowDefinition().getFlowSteps().size(); i++) {
            StepUsageDeclaration stepUsageDeclaration = flowExecution.getFlowDefinition().getFlowSteps().get(i);
            System.out.println("Starting to execute step: " + stepUsageDeclaration.getFinalStepName());
            StepResult stepResult = stepUsageDeclaration.getStepDefinition().invoke(context);
            System.out.println("Done executing step: " + stepUsageDeclaration.getFinalStepName() + ". Result: " + stepResult);
            // check if should continue etc..
            String summery = stepUsageDeclaration.getStepDefinition().getSummery();
            if (!summery.isEmpty()) {
                System.out.println("Step " + stepUsageDeclaration.getFinalStepName() + " summery: " + summery);
            }
            System.out.println("Step " + stepUsageDeclaration.getFinalStepName() + " logs: ");
            List<String> logs = context.getStepLogLines(stepUsageDeclaration.getFinalStepName());
            for (String log : logs) {
                System.out.println(log);
            }
            if (stepResult == StepResult.FAILURE) {
                result = FlowExecutionResult.FAILURE;
                if (!stepUsageDeclaration.skipIfFail()) {
                    break;
                }
            }
            if (stepResult == StepResult.WARNING) {
                result = FlowExecutionResult.WARNING;
            }
        }
        flowExecution.setFlowExecutionResult(result);
        System.out.println("Outputs: " + context.getAllOutputs(flowExecution.getFlowDefinition().getFlowFormalOutputs()));
        System.out.println("End execution of flow " + flowExecution.getFlowDefinition().getName() + " [ID: " + flowExecution.getUniqueId() + "]. Status: " + flowExecution.getFlowExecutionResult());
    }
}
