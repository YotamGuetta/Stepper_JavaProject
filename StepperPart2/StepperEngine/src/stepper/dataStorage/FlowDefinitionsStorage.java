package stepper.dataStorage;

import stepper.flow.definition.api.FlowDefinition;

import java.util.ArrayList;
import java.util.List;

public class FlowDefinitionsStorage {
    private final List<FlowDefinition> flowDefinitions;
    public FlowDefinitionsStorage(){
        flowDefinitions = new ArrayList<>();
    }
    public void AddFlowDefinition(FlowDefinition flowDefinition){
        flowDefinitions.add(flowDefinition);
    }
    public FlowDefinition GetFlowDefinition(String flowName){
        for (FlowDefinition flowDefinition : flowDefinitions){
            if(flowDefinition.getName().equals(flowName)){
                return flowDefinition;
            }
        }
        return null;
    }
}
