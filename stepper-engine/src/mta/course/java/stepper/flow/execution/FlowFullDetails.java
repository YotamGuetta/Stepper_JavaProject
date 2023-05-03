package mta.course.java.stepper.flow.execution;

import mta.course.java.stepper.flow.definition.api.StepUsageDeclaration;
import mta.course.java.stepper.step.StepDefinitionRegistry;
import mta.course.java.stepper.step.api.DataDefinitionDeclaration;
import mta.course.java.stepper.step.api.DataNecessity;
import mta.course.java.stepper.step.api.StepDefinition;

import java.util.List;
import java.util.Map;

public class FlowFullDetails {
    String historyData;
    String uuid;
    String flowName;
    long flowRunTime;
    public String getHistoryData(){
        return historyData;
    }
    public void SaveData(FlowExecution flowExecution){
        uuid = flowExecution.getThisRunUniqueID();
        flowName = flowExecution.getFlowDefinition().getName();
        flowRunTime = flowExecution.getFlowRunTime();
        StringBuilder details = new StringBuilder();
        details.append("Flow Name: ").append(flowName).append("\n");
        details.append("Flow Unique ID: ").append(uuid).append("\n");
        details.append("Start Time: ").append(flowExecution.getTimeStamp()).append("\n");
        details.append("Flow Result: ").append(flowExecution.getFlowExecutionResult().toString()).append("\n");
        details.append("Flow Run Time: ").append(flowRunTime).append("ms\n");

        details.append("Free Inputs: ").append("\n");
        // Map<String, Object> inputs = flowExecution.getFreeInputs();
        // for(String key: inputs.keySet())
        // details.append("Name: ").append(key).append("/n");

        List<DataDefinitionDeclaration> inputsDefinition = flowExecution.getFlowDefinition().getFlowFreeInputs();
        StringBuilder optionalInputs = new StringBuilder();
        for (DataDefinitionDeclaration input : inputsDefinition){
            StringBuilder detailsToAdd = new StringBuilder();
            detailsToAdd.append("Name: ").append(input.getName()).append("\n");
            detailsToAdd.append("Type: ").append(input.dataDefinition().getType()).append("\n");
            detailsToAdd.append("Context: ").append(input.dataDefinition()).append("\n");
            detailsToAdd.append("Necessity: ").append(input.necessity().toString()).append("\n");
            if(input.necessity() == DataNecessity.MANDATORY)
                details.append(detailsToAdd);
            else
                optionalInputs.append(detailsToAdd);
        }
        details.append(optionalInputs);

        details.append("Outputs: ").append("\n");
        List<String> outputsDefinition = flowExecution.getFlowDefinition().getFlowFormalOutputs();
        for (String output : outputsDefinition){
            StepDefinition outputData=StepDefinitionRegistry.valueOf(output);
            details.append("Name: ").append(output).append("\n");
            details.append("Type: ").append(output).append("\n");
            details.append("Context: ").append(output).append("\n");
        }
        Map<String,String> summery = flowExecution.getSummeryOfAFlowRun();
        List<StepUsageDeclaration> steps=flowExecution.getFlowDefinition().getFlowSteps();
        for (StepUsageDeclaration step : steps){
            details.append("Name: ").append(step.getFinalStepName()).append("\n");
            details.append("Time: ").append("\n");
            details.append("Result: ").append("\n");
            details.append("Summery: ").append(summery.get(step.getFinalStepName())).append("\n");

        }

        details.append("Logs").append(flowExecution.getLogOfAFlowRun()).append("\n");

        historyData = details.toString();
    }

}
