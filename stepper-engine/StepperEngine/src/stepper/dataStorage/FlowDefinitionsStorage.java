package stepper.dataStorage;

import stepper.flow.definition.api.FlowDefinition;

import java.util.ArrayList;
import java.util.List;

public class FlowDefinitionsStorage {
    private ClientData clientData;
    private final List<FlowDefinition> flowDefinitions;
    public FlowDefinitionsStorage(){
        flowDefinitions = new ArrayList<>();
    }
    public FlowDefinitionsStorage(ClientData clientData){
        flowDefinitions = new ArrayList<>();
        this.clientData = clientData;
    }
    public void AddFlowDefinition(FlowDefinition flowDefinition){
        if(clientData != null){
            clientData.AddFlowDefinitionToDefaultRoles(flowDefinition);
        }
        flowDefinitions.add(flowDefinition);
    }
    public List<FlowDefinition> getFlowDefinitions(){
        return flowDefinitions;
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
