package stepper.dataStorage;

import stepper.clients.ClientRole;
import stepper.clients.ClientUser;
import stepper.flow.definition.api.FlowDefinition;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public  class ClientData {
    private static final String READ_ONLY  = "Read Only Flows";
    private static final String ALL  = "All Flows";
    private  Map<String, ClientUser> userMap;
    private  Map<String, ClientRole> roleMap;
    public ClientData(){
        userMap = new HashMap<>();
        roleMap = new HashMap<>();
        addDefaultData();
    }
    private void addDefaultData(){
        roleMap.put(READ_ONLY, new ClientRole(READ_ONLY,"Sees all the read only flows", new HashSet<>()));
        roleMap.put(ALL, new ClientRole(ALL,"Sees all flows", new HashSet<>()));
    }
    public void AddFlowDefinitionToDefaultRoles(FlowDefinition flowDefinition){
        if(flowDefinition.isFlowReadOnly()) {
            roleMap.get(READ_ONLY).AddFlowDefinitionToRole(flowDefinition);
        }
        roleMap.get(ALL).AddFlowDefinitionToRole(flowDefinition);
    }
    public Map<String, ClientRole> getRoleMap() {
        return roleMap;
    }

    public Map<String, ClientUser> getUserMap() {
        return userMap;
    }
    public void AddRoleToMap(ClientRole clientRole) {
        roleMap.put(clientRole.getName(), clientRole);
    }
}
