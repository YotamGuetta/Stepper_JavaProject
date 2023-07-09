package stepper.dataStorage;

import javafx.util.Pair;
import stepper.flow.definition.api.StepUsageDeclaration;
import stepper.flow.execution.FlowExecution;
import stepper.flow.execution.FlowExecutionResult;
import stepper.flow.execution.context.StepExecutionContext;
import stepper.step.api.DataCapsuleImpl;

import java.sql.Timestamp;
import java.util.*;

public class FlowFullDetails {
    StepExecutionContext context;
    FlowExecution flowExecution;
    String uuid;
    String flowName;
    String startTime;
    long flowRunTime;
    FlowExecutionResult flowResult;
    private Map<String, stepDetails>  listOfStepsDetails;
    public StepExecutionContext GetStepExecutionContext(){
        return context;
    }
    public FlowExecution getFlowExecution(){
        return flowExecution;
    }
    public Pair<List<StatisticData>, List<StatisticData>> GetStatistics(){
        List<StatisticData> flows = new ArrayList<>();
        List<StatisticData> steps = new ArrayList<>();

        flows.add(new StatisticData(flowExecution.getFlowDefinition().getName(),1,(double) flowExecution.getFlowRunTime()));
        for(StepUsageDeclaration step : flowExecution.getFlowDefinition().getFlowSteps()) {
            steps.add(new StatisticData(step.getFinalStepName(), 1,(double) step.getStepRunTime()));
        }
        return new Pair<>(flows, steps);
    }
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
    public Map<String, stepDetails> getSteps(){
        if(listOfStepsDetails != null){
            return listOfStepsDetails;
        }
        Map<String, stepDetails> stepsResults = new HashMap<>();
        List<StepUsageDeclaration> steps = flowExecution.getFlowDefinition().getFlowSteps();
        Calendar stepStartTime = Calendar.getInstance();
        stepStartTime.setTime(Timestamp.valueOf(startTime));
        for (StepUsageDeclaration step : steps) {
            stepsResults.put(step.getFinalStepName(), new stepDetails(step, flowExecution, context, stepStartTime));
            stepStartTime.add(Calendar.MILLISECOND, (int)step.getStepRunTime());
        }
        listOfStepsDetails = stepsResults;
        return stepsResults;
    }
    public String getLogs(){
        return flowExecution.getLogOfAFlowRun();
    }
    public List<Map<String, Object>> getAStepDetails(String stepName){
        List<Map<String, Object>> stepDetails = new ArrayList<>();
        stepDetails singleStepDetails =  getSteps().get(stepName);
        stepDetails.add(singleStepDetails.GetStepInfo());
        stepDetails.add(singleStepDetails.GetStepInputs());
        stepDetails.add(singleStepDetails.GetStepOutputs());
        return stepDetails;
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
