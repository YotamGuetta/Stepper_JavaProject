package mta.course.java.stepper.flow.execution;

import mta.course.java.stepper.dd.api.DataDefinition;
import mta.course.java.stepper.flow.definition.api.FlowDefinition;
import mta.course.java.stepper.step.api.DataDefinitionDeclaration;
import mta.course.java.stepper.step.api.DataNecessity;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    // lots more data that needed to be stored while flow is being executed...

    public FlowExecution(String uniqueId, FlowDefinition flowDefinition) {
        this.uniqueId = uniqueId;
        this.flowDefinition = flowDefinition;
        this.freeInputs = new HashMap<>();
        logLines = new StringBuilder();
        summeryLines = new HashMap<>();

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
    public void storeLogsOfAFlowRun(String flowName, String logs) {
        logLines.append(logs).append("\n");
        System.out.println("logs:");
        System.out.println(logs);
        System.out.println();
    }
    public String getLogOfAFlowRun(){
        return logLines.toString();
    }
    public void storeSummeryOfAFlowRun(String flowName, String summery) {
        summeryLines.put(flowName, summery);
        System.out.println("summery:");
        System.out.println(summery);
        System.out.println();
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
        List<DataDefinitionDeclaration> flowFreeInputs = flowDefinition.getFlowFreeInputs();
        for (DataDefinitionDeclaration input : flowFreeInputs) {
            if (input.necessity() == DataNecessity.MANDATORY && !freeInputs.containsKey(input.getName()))
                return false;
        }
        return true;
    }

    public Object getInputValue(String inputName) {
        return freeInputs.get(inputName);
    }
}
