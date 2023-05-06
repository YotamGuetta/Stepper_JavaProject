package mta.course.java.stepper.flow.execution.runner;

import mta.course.java.stepper.flow.definition.api.StepUsageDeclaration;
import mta.course.java.stepper.flow.execution.FlowExecution;
import mta.course.java.stepper.flow.execution.FlowExecutionResult;
import mta.course.java.stepper.flow.execution.FlowFullDetails;
import mta.course.java.stepper.flow.execution.context.StepExecutionContext;
import mta.course.java.stepper.flow.execution.context.StepExecutionContextImpl;
import mta.course.java.stepper.step.api.DataCapsuleImpl;
import mta.course.java.stepper.step.api.StepResult;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public class FLowExecutor {

    public FlowFullDetails executeFlow(FlowExecution flowExecution) {
        long startTime = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(startTime);
        flowExecution.setTimeStamp(timestamp);
        UUID uuid = UUID.randomUUID();
        flowExecution.setThisRunUniqueID(uuid.toString());

        StepExecutionContext context = new StepExecutionContextImpl(flowExecution.getFlowDefinition()); // actual object goes here...

        FlowExecutionResult result = FlowExecutionResult.SUCCESS;
        StringBuilder logs = new StringBuilder();
        StringBuilder summery = new StringBuilder();

        context.assignCustomMapping(flowExecution.getFlowDefinition().getFullCustomMapping());
        for (String key : flowExecution.getFreeInputs().keySet()) {
            context.storeDataValue(key, "", flowExecution.getFreeInputs().get(key));
        }
        // start actual execution
        for (int i = 0; i < flowExecution.getFlowDefinition().getFlowSteps().size(); i++) {
            StepUsageDeclaration stepUsageDeclaration = flowExecution.getFlowDefinition().getFlowSteps().get(i);

            long singleStepRunTime = System.currentTimeMillis();
            StepResult stepResult = StepResult.FAILURE;
            try {
                stepResult = stepUsageDeclaration.getStepDefinition().invoke(context, stepUsageDeclaration.getFinalStepName());
            }
            catch (Exception e){
                result = FlowExecutionResult.FAILURE;
                if (!stepUsageDeclaration.skipIfFail()) {
                    break;
                }
            }
            singleStepRunTime = System.currentTimeMillis() - singleStepRunTime;
            stepUsageDeclaration.setStepRunTime(singleStepRunTime);
            stepUsageDeclaration.setStepResult(stepResult.toString());

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
        System.out.println("Flow unique ID:" + uuid);
        System.out.println("Flow name"+ flowExecution.getFlowDefinition().getName());
        System.out.println("Flow result: "+ result);
        System.out.println("Outputs:");
        try {
            List<String> outputsNames = flowExecution.getFlowDefinition().getFlowFormalOutputs();
            List<String> formalOutputs = context.getAllOutputs(outputsNames);
            for(int i=0; i< outputsNames.size(); i++) {
                DataCapsuleImpl outputData = flowExecution.getFlowDefinition().getOutputDataCapsule(outputsNames.get(i));
                if(outputData != null) {
                    System.out.println(outputData.getDataDefinitionDeclaration().userString() + " : " + formalOutputs.get(i));
                }
                else
                    throw  new Exception("Failed to get output");
            }
        } catch (Exception e) {
            System.out.println("Failed to get outputs");
        }
        long endTime = System.currentTimeMillis();
        flowExecution.setFlowRunTime(endTime - startTime);
        FlowFullDetails details = new FlowFullDetails();
        details.SaveData(flowExecution, context);
        return details;
    }
}
