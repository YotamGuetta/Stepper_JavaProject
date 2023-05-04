package mta.course.java.stepper.flow.execution;

import mta.course.java.stepper.flow.definition.api.StepUsageDeclaration;
import mta.course.java.stepper.flow.execution.context.StepExecutionContext;
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
    public void SaveData(FlowExecution flowExecution, StepExecutionContext context){
        uuid = flowExecution.getThisRunUniqueID();
        flowName = flowExecution.getFlowDefinition().getName();
        flowRunTime = flowExecution.getFlowRunTime();
        StringBuilder details = new StringBuilder();
        details.append("Flow Name: ").append(flowName).append("\n");
        details.append("Flow Unique ID: ").append(uuid).append("\n");
        details.append("Start Time: ").append(flowExecution.getTimeStamp()).append("\n");
        details.append("Flow Result: ").append(flowExecution.getFlowExecutionResult().toString()).append("\n");
        details.append("Flow Run Time: ").append(flowRunTime).append("ms\n");

        details.append("\nFree Inputs: ").append("\n");
        // Map<String, Object> inputs = flowExecution.getFreeInputs();
        // for(String key: inputs.keySet())
        // details.append("Name: ").append(key).append("/n");

        List<DataDefinitionDeclaration> inputsDefinition = flowExecution.getFlowDefinition().getFlowFreeInputs();
        Map<String, Object> inputsValues = flowExecution.getFreeInputs();
        StringBuilder optionalInputs = new StringBuilder();

        for (DataDefinitionDeclaration input : inputsDefinition){
            Object value = inputsValues.get(input.getName());
            if(value != null) {
                StringBuilder detailsToAdd = new StringBuilder();
                detailsToAdd.append("Name: ").append(input.dataDefinition().getName()).append("\n");
                detailsToAdd.append("Type: ").append(input.dataDefinition().getType().getSimpleName()).append("\n");
                detailsToAdd.append("Context: ").append(value).append("\n");
                detailsToAdd.append("Necessity: ").append(input.necessity().toString()).append("\n");
                if (input.necessity() == DataNecessity.MANDATORY)
                    details.append(detailsToAdd);
                else
                    optionalInputs.append(detailsToAdd);
            }
        }
        details.append(optionalInputs);

        List<String> outputsContext = context.getAllOutputs(flowExecution.getFlowDefinition().getFlowFormalOutputs());

        details.append("\nOutputs: ").append("\n");
        List<StepUsageDeclaration> steps = flowExecution.getFlowDefinition().getFlowSteps();
        int index =0;
        for(StepUsageDeclaration step : steps){
           List<DataDefinitionDeclaration> outputs =  step.getStepDefinition().outputs();
           for (DataDefinitionDeclaration output : outputs){
               details.append("Name: ").append(output.getName()).append("\n");
               details.append("Type: ").append(output.dataDefinition().getType().getSimpleName()).append("\n");
               details.append("Context: ").append(outputsContext.get(index)).append("\n");
               index++;
           }

        }
        Map<String,String> summery = flowExecution.getSummeryOfAFlowRun();
        details.append("\nSteps:\n");
        for (StepUsageDeclaration step : steps){
            details.append("Name: ").append(step.getFinalStepName()).append("\n");
            details.append("Time: ").append(step.getStepRunTime()).append("ms\n");
            details.append("Result: ").append(step.getStepResult()).append("\n");
            details.append("Summery: ").append(summery.get(step.getFinalStepName())).append("\n");

        }

        details.append("\nLogs:\n").append(flowExecution.getLogOfAFlowRun()).append("\n");

        historyData = details.toString();
    }

}
