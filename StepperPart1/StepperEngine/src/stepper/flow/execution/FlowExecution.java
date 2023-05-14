package stepper.flow.execution;

import stepper.flow.definition.api.FlowDefinition;
import stepper.flow.execution.FlowExecutionResult;
import stepper.step.api.DataCapsuleImpl;
import stepper.step.api.DataNecessity;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class FlowExecution {

    private final String uniqueId;
    private final FlowDefinition flowDefinition;
    private FlowExecutionResult flowExecutionResult;
    private final Map<String, Object> freeInputs;
    private StringBuilder logLines;
    private final Map<String, String> summeryLines;
    private String uuidAsString;
    private Timestamp timeStamp;
    private long flowRunTime;
    private final Map<String, String> flowFormalOutputs;
    // lots more data that needed to be stored while flow is being executed...

    public FlowExecution(String uniqueId, FlowDefinition flowDefinition) {
        this.uniqueId = uniqueId;
        this.flowDefinition = flowDefinition;
        this.freeInputs = new HashMap<>();
        logLines = new StringBuilder();
        summeryLines = new HashMap<>();
        flowFormalOutputs = new HashMap<>();

    }
    public void storeFormalOutput(String userName, String value){
        flowFormalOutputs.put(userName,value);
    }
    public Map<String, String> getFlowFormalOutputs(){
        return  flowFormalOutputs;
    }
    public  void clearFlowData(){
        this.freeInputs.clear();
        logLines = new StringBuilder();
        summeryLines.clear();
    }
    public void setTimeStamp(Timestamp time){
        timeStamp=time;
    }
    public void setFlowRunTime(long time){
        flowRunTime=time;
    }
    public long getFlowRunTime(){
        return flowRunTime;
    }
    public String getTimeStamp(){
        return timeStamp.toString();
    }
    public void setThisRunUniqueID(String id){
        uuidAsString = id;
    }public String getThisRunUniqueID(){
        return uuidAsString;
    }
    public void storeLogsOfAFlowRun(String logs) {
        logLines.append(logs).append("\n");
    }
    public String getLogOfAFlowRun(){
        return logLines.toString();
    }
    public void storeSummeryOfAFlowRun(String flowName, String summery) {
        summeryLines.put(flowName, summery);
    }
    public Map<String,String> getSummeryOfAFlowRun(){
        return summeryLines;
    }
    public void addFreeInput(String name, Object value) {
        freeInputs.put(name, value);
    }

    public Map<String, Object> getFreeInputs() {
        return freeInputs;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public FlowDefinition getFlowDefinition() {
        return flowDefinition;
    }

    public FlowExecutionResult getFlowExecutionResult() {
        return flowExecutionResult;
    }

    public void setFlowExecutionResult(FlowExecutionResult result) {
        flowExecutionResult = result;
    }

    public boolean CheckIfExecutable() {
        Map<String, DataCapsuleImpl> flowFreeInputs = flowDefinition.getFlowFreeInputs();
        for (DataCapsuleImpl input : flowFreeInputs.values()) {
            if (input.getDataDefinitionDeclaration().necessity() == DataNecessity.MANDATORY && !freeInputs.containsKey(input.getFinalName()))
                return false;
        }
        return true;
    }

}
