package stepper.flow.execution;

import stepper.flow.definition.api.StepUsageDeclaration;
import stepper.flow.execution.context.StepExecutionContext;
import stepper.step.api.DataCapsuleImpl;
import stepper.step.api.DataNecessity;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

public class FlowFullDetails {
    StepExecutionContext context;
    FlowExecution flowExecution;
    String uuid;
    String flowName;
    String startTime;
    long flowRunTime;
    FlowExecutionResult flowResult;
    private List<Map<String,String>> listOfStepsDetails;
    public String getFlowResult(){return flowResult.toString();}
    public String getFlowName(){
        return  flowName;
    }
    public String getUuid(){
        return uuid;
    }
    public String getStartTime(){
        return startTime;
    }
    public List<Map<String,String>> getFreeInputs(){
        List<Map<String,String>> freeInputs = new ArrayList<>();
        Map<String, DataCapsuleImpl> inputsDefinition = flowExecution.getFlowDefinition().getFlowFreeInputs();
        Map<String, Object> inputsValues = flowExecution.getFreeInputs();

        for (String key : inputsDefinition.keySet()){
            Object value = inputsValues.get(key);
            if(value != null) {
                Map<String,String> freeInputMap = new HashMap<>();
                freeInputMap.put("Name", key);
                freeInputMap.put("Type", inputsDefinition.get(key).getDataDefinitionDeclaration().dataDefinition().getType().getSimpleName());
                freeInputMap.put("Context", value.toString());
                freeInputMap.put("Necessity", inputsDefinition.get(key).getDataDefinitionDeclaration().necessity().toString());
                freeInputs.add(freeInputMap);
            }
        }
        return  freeInputs;
    }
    public List<Map<String,String>> getOutputs(){
        List<Map<String,String>> outputsResults = new ArrayList<>();
        List<DataCapsuleImpl> outputs =flowExecution.getFlowDefinition().getAllFlowOutputs();
        for (DataCapsuleImpl output : outputs){
            Map<String,String> outputResulMap = new HashMap<>();
            outputResulMap.put("Name", output.getFinalName());
            outputResulMap.put("Type", output.getDataDefinitionDeclaration().dataDefinition().getType().getSimpleName());
            outputResulMap.put("Context", context.getOutput(output.getFinalName()));
            outputsResults.add(outputResulMap);
        }
        return outputsResults;
    }
    public List<Map<String,String>> getSteps(){
        if(listOfStepsDetails != null){
            return listOfStepsDetails;
        }
        List<Map<String,String>> stepsResults = new ArrayList<>();
        List<StepUsageDeclaration> steps = flowExecution.getFlowDefinition().getFlowSteps();
        Map<String,String> summery = flowExecution.getSummeryOfAFlowRun();
        for (StepUsageDeclaration step : steps){
            Map<String,String> stepResultMap = new HashMap<>();
            stepResultMap.put("Name", step.getFinalStepName());
            stepResultMap.put("Time", Long.toString(step.getStepRunTime()));
            stepResultMap.put("Result", step.getStepResult() != null ? step.getStepResult(): "FAILED");
            String stepSummery = summery.get(step.getFinalStepName());
            stepResultMap.put("Summery", (stepSummery != null) ? stepSummery : "");

            stepsResults.add(stepResultMap);
        }
        listOfStepsDetails = stepsResults;
        return stepsResults;
    }
    public String getLogs(){
        return flowExecution.getLogOfAFlowRun();
    }
    public Map<String, String> getAStepDetails(String stepName){
        Calendar stepStartTime = Calendar.getInstance();
        stepStartTime.setTime(Timestamp.valueOf(startTime));
        Map<String, String> desiredStepResult = new HashMap<>();
        List<Map<String,String>> stepsResults = getSteps();
        for(Map<String,String> aStepResult : stepsResults){
            if(aStepResult.get("Name").equals(stepName)){
                desiredStepResult.putAll(aStepResult);
                desiredStepResult.put("Start Time", stepStartTime.getTime().toString());
                // End Time
                stepStartTime.add(Calendar.MILLISECOND, (int)Long.parseLong(aStepResult.get("Time")));
                desiredStepResult.put("End Time", stepStartTime.getTime().toString());
            }
            else{
                stepStartTime.add(Calendar.MILLISECOND, (int)Long.parseLong(aStepResult.get("Time")));
            }
        }
        return desiredStepResult;
    }
    public void SaveData(FlowExecution flowExecution, StepExecutionContext context) {
        this.flowExecution = flowExecution;
        this.context = context;
        uuid = flowExecution.getThisRunUniqueID();
        flowName = flowExecution.getFlowDefinition().getName();
        flowRunTime = flowExecution.getFlowRunTime();
        startTime = flowExecution.getTimeStamp();
        flowResult = flowExecution.getFlowExecutionResult();
    }

}
