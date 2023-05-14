package stepper.flow.execution;

import stepper.flow.definition.api.StepUsageDeclaration;
import stepper.flow.execution.context.StepExecutionContext;
import stepper.step.api.DataCapsuleImpl;
import stepper.step.api.DataNecessity;

import java.util.List;
import java.util.Map;

public class FlowFullDetails {
    String historyData;
    String uuid;
    String flowName;
    String startTime;
    long flowRunTime;
    public String getHistoryData(){
        return historyData;
    }
    public String getFlowName(){
        return  flowName;
    }
    public String getUuid(){
        return uuid;
    }
    public String getStartTime(){
        return startTime;
    }
    public void SaveData(FlowExecution flowExecution, StepExecutionContext context){

        uuid = flowExecution.getThisRunUniqueID();
        flowName = flowExecution.getFlowDefinition().getName();
        flowRunTime = flowExecution.getFlowRunTime();
        startTime = flowExecution.getTimeStamp();
        StringBuilder details = new StringBuilder();
        details.append("Flow Name: ").append(flowName).append("\n");
        details.append("Flow Unique ID: ").append(uuid).append("\n");
        details.append("Start Time: ").append(startTime).append("\n");
        details.append("Flow Result: ").append(flowExecution.getFlowExecutionResult().toString()).append("\n");
        details.append("Flow Run Time: ").append(flowRunTime).append("ms\n");

        details.append("\nFree Inputs: ").append("\n");
        // Map<String, Object> inputs = flowExecution.getFreeInputs();
        // for(String key: inputs.keySet())
        // details.append("Name: ").append(key).append("/n");

        Map<String, DataCapsuleImpl> inputsDefinition = flowExecution.getFlowDefinition().getFlowFreeInputs();
        Map<String, Object> inputsValues = flowExecution.getFreeInputs();
        StringBuilder optionalInputs = new StringBuilder();

        for (String key : inputsDefinition.keySet()){
            Object value = inputsValues.get(key);
            if(value != null) {
                StringBuilder detailsToAdd = new StringBuilder();
                detailsToAdd.append("Name: ").append(key).append("\n");
                detailsToAdd.append("Type: ").append(inputsDefinition.get(key).getDataDefinitionDeclaration().dataDefinition().getType().getSimpleName()).append("\n");
                detailsToAdd.append("Context: ").append(value).append("\n");
                detailsToAdd.append("Necessity: ").append(inputsDefinition.get(key).getDataDefinitionDeclaration().necessity().toString()).append("\n");
                if (inputsDefinition.get(key).getDataDefinitionDeclaration().necessity() == DataNecessity.MANDATORY)
                    details.append(detailsToAdd);
                else
                    optionalInputs.append(detailsToAdd);
            }
        }
        details.append(optionalInputs);


        List<DataCapsuleImpl> outputs =flowExecution.getFlowDefinition().getAllFlowOutputs();
        details.append("\nOutputs: ").append("\n");
           for (DataCapsuleImpl output : outputs){
               details.append("Name: ").append(output.getFinalName()).append("\n");
               details.append("Type: ").append(output.getDataDefinitionDeclaration().dataDefinition().getType().getSimpleName()).append("\n");
               details.append("Context: ").append(context.getOutput(output.getFinalName())).append("\n");
           }

        List<StepUsageDeclaration> steps = flowExecution.getFlowDefinition().getFlowSteps();
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
