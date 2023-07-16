package stepper.clients;

import stepper.flow.definition.api.FlowDefinition;

import java.util.List;
import java.util.Set;

public class ClientRole {
    private final String roleName;
    private final String roleDetails;
    private Set<FlowDefinition> availableFlows;
    public ClientRole(String name,String roleDetails, Set<FlowDefinition> availableFlows){
        roleName = name;
        this.roleDetails = roleDetails;
        this.availableFlows = availableFlows;
    }
    public Set<FlowDefinition> GetAvailableFlows(){
        return availableFlows;
    }
    public void AddFlowDefinitionToRole(FlowDefinition flowDefinition){
        availableFlows.add(flowDefinition);
    }
    public boolean IsAvailableFlow(FlowDefinition flowDefinition){
        return availableFlows.contains(flowDefinition);
    }
    public void SetNewAvailableFlowDefinitions(Set<FlowDefinition> availableFlows){
        this.availableFlows = availableFlows;
    }
    public String getRoleDetails() {
        return roleDetails;
    }
    public String getName() {
        return roleName;
    }
}
