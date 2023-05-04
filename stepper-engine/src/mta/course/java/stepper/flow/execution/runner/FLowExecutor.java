package mta.course.java.stepper.flow.execution.runner;

import mta.course.java.stepper.flow.definition.api.StepUsageDeclaration;
import mta.course.java.stepper.flow.execution.FlowExecution;
import mta.course.java.stepper.flow.execution.FlowExecutionResult;
import mta.course.java.stepper.flow.execution.FlowFullDetails;
import mta.course.java.stepper.flow.execution.context.StepExecutionContext;
import mta.course.java.stepper.flow.execution.context.StepExecutionContextImpl;
import mta.course.java.stepper.step.api.StepResult;

import java.sql.Timestamp;
import java.util.UUID;

public class FLowExecutor {

    public FlowFullDetails executeFlow(FlowExecution flowExecution) {
        long startTime = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(startTime);
        flowExecution.setTimeStamp(timestamp);
        UUID uuid = UUID.randomUUID();
        flowExecution.setThisRunUniqueID(uuid.toString());
        System.out.println("Starting execution of flow " + flowExecution.getFlowDefinition().getName() + " [ID: " + flowExecution.getUniqueId() + "]");

        StepExecutionContext context = new StepExecutionContextImpl(flowExecution.getFlowDefinition().getFlowSteps()); // actual object goes here...

        FlowExecutionResult result = FlowExecutionResult.SUCCESS;
        StringBuilder logs = new StringBuilder();
        StringBuilder summery = new StringBuilder();
        // populate context with all free inputs (mandatory & optional) that were given from the user
        // (typically stored on top of the flow execution object)

        for (String key : flowExecution.getFreeInputs().keySet()) {
            context.storeDataValue(key, flowExecution.getFreeInputs().get(key));
        }
        // start actual execution
        for (int i = 0; i < flowExecution.getFlowDefinition().getFlowSteps().size(); i++) {
            StepUsageDeclaration stepUsageDeclaration = flowExecution.getFlowDefinition().getFlowSteps().get(i);

            System.out.println("Starting to execute step: " + stepUsageDeclaration.getFinalStepName());
            long singleStepRunTime = System.currentTimeMillis();
            StepResult stepResult = stepUsageDeclaration.getStepDefinition().invoke(context);
            singleStepRunTime = System.currentTimeMillis() - singleStepRunTime;
            stepUsageDeclaration.setStepRunTime(singleStepRunTime);
            stepUsageDeclaration.setStepResult(stepResult.toString());

            System.out.println("Done executing step: " + stepUsageDeclaration.getFinalStepName() + ". Result: " + stepResult);
            String stepSummery = stepUsageDeclaration.getStepDefinition().getSummery();
            if (!(stepSummery == null))
                summery.append(stepSummery).append("\n");

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
        logs.append(context.getStepLogLines());
        flowExecution.storeLogsOfAFlowRun(flowExecution.getUniqueId(), logs.toString());
        flowExecution.storeSummeryOfAFlowRun(flowExecution.getUniqueId(), summery.toString());
        flowExecution.setFlowExecutionResult(result);
        try {
            System.out.println("Outputs: " + context.getAllOutputs(flowExecution.getFlowDefinition().getFlowFormalOutputs()));
        } catch (Exception e) {
            System.out.println("Failed to get outputs");
        }
        System.out.println("End execution of flow " + flowExecution.getFlowDefinition().getName() + " [ID: " + flowExecution.getUniqueId() + "]. Status: " + flowExecution.getFlowExecutionResult());
        long endTime = System.currentTimeMillis();
        flowExecution.setFlowRunTime(endTime - startTime);
        FlowFullDetails details = new FlowFullDetails();
        details.SaveData(flowExecution, context);
        return details;
    }
}
