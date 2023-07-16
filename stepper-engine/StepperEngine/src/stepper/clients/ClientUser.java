package stepper.clients;

import stepper.flow.definition.api.FlowDefinition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientUser {
    private String userName;
    private Set<ClientRole> roles;
    private int numberOfFlowRuns;
    public ClientUser(String name, Set<ClientRole> roles){
        userName = name;
        this.roles = roles;
        numberOfFlowRuns = 0;
    }
    public Set<FlowDefinition> getClientAvailableFlows(){
        Set<FlowDefinition> availableFlows = new HashSet<>();
        for(ClientRole role : roles){
            availableFlows.addAll(role.GetAvailableFlows());
        }
        return availableFlows;
    }

    public String getName() {
        return userName;
    }
}
