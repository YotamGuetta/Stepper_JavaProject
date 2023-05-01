package mta.course.java.stepper.flow.execution;

import mta.course.java.stepper.flow.definition.api.FlowDefinition;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class FlowExecution {

    private final String uniqueId;
    private final FlowDefinition flowDefinition;
    private Duration totalTime;
    private FlowExecutionResult flowExecutionResult;
    private Map<String, Object> freeInputs;

    // lots more data that needed to be stored while flow is being executed...

    public FlowExecution(String uniqueId, FlowDefinition flowDefinition) {
        this.uniqueId = uniqueId;
        this.flowDefinition = flowDefinition;
        this.freeInputs =  new HashMap<>();
    }

    public  void addFreeInput(String name, Object value){
        freeInputs.put(name, value);
    }
    public  Map<String, Object> getFreeInputs(){
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

}
