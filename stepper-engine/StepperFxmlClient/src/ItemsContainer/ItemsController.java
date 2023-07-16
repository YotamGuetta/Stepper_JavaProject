package ItemsContainer;

import ClientUI.Flow.FlowController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import main.MainController;
import stepper.clients.ClientRole;
import stepper.clients.ClientUser;
import stepper.dataStorage.ClientData;
import stepper.dataStorage.FlowDefinitionsStorage;
import stepper.flow.definition.api.FlowDefinition;
import stepper.flow.definition.api.StepUsageDeclaration;
import stepper.step.api.DataDefinitionDeclaration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemsController {
    private String newRoleName;
    private String newRoleDescription;
    private List<CheckBox> checkBoxList;
    private FlowDefinitionsStorage flowDefinitionsStorage;
    private ClientData clientData;
    private MainController mainController;
    private FlowDefinition selectedFlow;
    private ClientRole selectedRole;
    private ClientUser selectedUser;
    private ItemsTypeRegistry itemsType;
    public VBox ItemsContainer;
    public GridPane ItemFullDetails;
    public void SetMainController(MainController mainController){
        this.mainController = mainController;
    }
    public void SetItemsType(ItemsTypeRegistry itemsType){
        this.itemsType = itemsType;
    }
    private void clearFlowFullData(){
        ItemFullDetails.getChildren().clear();
    }
    public void SetClientData(ClientData clientData){
        this.clientData = clientData;
    }
    public void SetFlowDefinitionsStorage(FlowDefinitionsStorage flowDefinitionsStorage){
        this.flowDefinitionsStorage = flowDefinitionsStorage;
    }
    public void ShowItems(){
        switch (itemsType){
            case FLOW:

                break;
            case ROLE:
                if(clientData !=null){
                    for(ClientRole role : clientData.getRoleMap().values()){
                        FlowController itemsController = loadSingleFXML();
                        itemsController.LoadNewRole(role);
                        itemsController.setItemsController(this);
                    }

                    Button newButton = new Button();
                    newButton.setText("New");
                    newButton.onMouseClickedProperty().set(this::creatNewRole);
                    newButton.prefWidth(90);
                    newButton.prefHeight(23);
                    newButton.setLineSpacing(2);
                    VBox vBox = new VBox();
                    vBox.setAlignment(Pos.CENTER_RIGHT);
                    vBox.setPadding(new Insets(8,8,8,0));
                    vBox.getChildren().add(newButton);
                    ItemsContainer.getChildren().add(vBox);
                }
                break;
            case USER:
                if(clientData !=null){
                    for(ClientUser user : clientData.getUserMap().values()){
                        FlowController itemsController = loadSingleFXML();
                        itemsController.LoadNewUser(user);
                        itemsController.setItemsController(this);
                    }
                }
                break;
        }
    }
    public FlowController loadSingleFXML() {
        FlowController flowController = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientUI/Flow/FlowSceneLayout.fxml"));
            Parent root = loader.load();
            ItemsContainer.getChildren().add(root);
            //root.setStyle("/Flow/FlowStyles.css");

            // If the SingleController class is needed for interacting with the contents of single.fxml
            flowController = loader.getController();

            ////flowController.setMainController(this);--------------------------------------
            // Perform any necessary setup or interaction with the SingleController
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }
        return flowController;
    }
    public void ShowFlowFullDetails(FlowDefinition flowDefinition) {
        selectedFlow = flowDefinition;
        int row = -1;
        clearFlowFullData();
        for (StepUsageDeclaration step : flowDefinition.getFlowSteps()) {
            ItemFullDetails.add(new Label("________________________________"), 0, ++row);
            ItemFullDetails.add(new Label("_______________________________"), 1, row);

            ItemFullDetails.add(new Label("Step Name"), 0, ++row);
            ItemFullDetails.add(new Label(step.getFinalStepName() + ":"), 1, row);

            ItemFullDetails.add(new Label("Step Inputs:"), 0, ++row);
            for (DataDefinitionDeclaration input : step.getStepDefinition().inputs()) {
                ItemFullDetails.add(new Label(selectedFlow.GetDataDefinitionAfterAliasing(input.getName(),step.getFinalStepName()) + ","), 0, ++row);
                ItemFullDetails.add(new Label(input.necessity().name()), 1, row);
            }
            ItemFullDetails.add(new Label("Step Outputs:"), 0, ++row);
            for (DataDefinitionDeclaration output : step.getStepDefinition().outputs()) {
                ItemFullDetails.add(new Label(selectedFlow.GetDataDefinitionAfterAliasing(output.getName(),step.getFinalStepName()) + ","), 0, ++row);
            }

        }
        makeButton(ItemsTypeRegistry.FLOW, ++row);

    }
    public void ShowRoleFullDetails(ClientRole role) {
        selectedRole = role;
        int row = -1;
        clearFlowFullData();
        ItemFullDetails.add(new Label("________________________________"), 0, ++row);
        ItemFullDetails.add(new Label("_______________________________"), 1, row);
        ItemFullDetails.add(new Label("flow Name:"), 0, ++row);
        ItemFullDetails.add(new Label(role.getName()), 1, row);
        ItemFullDetails.add(new Label("________________________________"), 0, ++row);
        ItemFullDetails.add(new Label("_______________________________"), 1, row);
        ItemFullDetails.add(new Label("flow Details:"), 0, ++row);
        ItemFullDetails.add(new Label(role.getRoleDetails()), 1, row);

        ItemFullDetails.add(new Label("________________________________"), 0, ++row);
        ItemFullDetails.add(new Label("_______________________________"), 1, row);
        ItemFullDetails.add(new Label("Role Flows:"), 0, ++row);
        checkBoxList = new ArrayList<>();
        if (flowDefinitionsStorage != null) {
            for (FlowDefinition flow : flowDefinitionsStorage.getFlowDefinitions()) {
                ItemFullDetails.add(new Label("________________________________"), 0, ++row);
                ItemFullDetails.add(new Label("_______________________________"), 1, row);
                CheckBox checkBox = new CheckBox(flow.getName());
                checkBox.setSelected(role.IsAvailableFlow(flow));
                ItemFullDetails.add(checkBox, 1, ++row);
                checkBoxList.add(checkBox);
            }
        }
        makeButton(ItemsTypeRegistry.ROLE, ++row);
    }
    public void ShowUserFullDetails(ClientUser user) {
        selectedUser = user;
        int row = -1;
        clearFlowFullData();
        for (FlowDefinition flow : user.getClientAvailableFlows()) {
            ItemFullDetails.add(new Label("________________________________"), 0, ++row);
            ItemFullDetails.add(new Label("_______________________________"), 1, row);

            ItemFullDetails.add(new Label("flow Name"), 0, ++row);
            ItemFullDetails.add(new Label(flow.getName()), 1, row);
        }
        makeButton(ItemsTypeRegistry.USER, ++row);
    }
    private void makeButton(ItemsTypeRegistry item, int row){
        Button startFlowButton = new Button();
        switch (itemsType){
            case FLOW:
                startFlowButton.setText("Start Flow");
                startFlowButton.onMouseClickedProperty().set(this::StartFlow);
            case ROLE:
                startFlowButton.setText("Save");
                startFlowButton.onMouseClickedProperty().set(this::saveRole);
                break;
            case USER:
                startFlowButton.setText("Save");
                startFlowButton.onMouseClickedProperty().set(this::saveUser);
        }
        startFlowButton.prefWidth(90);
        startFlowButton.prefHeight(23);

        ItemFullDetails.add(startFlowButton, 1, row);
        ItemFullDetails.add(new Label(), 0, row);
    }
    private  void saveRole(MouseEvent event){
        Set<FlowDefinition> flowDefinitionSet = new HashSet<>();
        for(CheckBox checkBox : checkBoxList){
            if(checkBox.isSelected()){
                flowDefinitionSet.add(flowDefinitionsStorage.GetFlowDefinition(checkBox.getText()));
            }
        }
        if(!clientData.getRoleMap().containsKey(selectedRole.getName())){
            selectedRole = new ClientRole(newRoleName, newRoleDescription, flowDefinitionSet);
            clientData.AddRoleToMap(selectedRole);
            ItemsContainer.getChildren().clear();
            ShowItems();
            ShowRoleFullDetails(selectedRole);
        }
        else{
            selectedRole.SetNewAvailableFlowDefinitions(flowDefinitionSet);
        }
    }
    private  void saveUser(MouseEvent event){
        //!mainController!
        //tabSelected = 1;
        //flowTabPane.getSelectionModel().select(tabSelected);
        //flowExecutionComponentController.StartFlowPreparations(selectedFlow);
    }
    @FXML
    private void StartFlow(MouseEvent event){
        //!mainController!
        //tabSelected = 1;
        //flowTabPane.getSelectionModel().select(tabSelected);
        //flowExecutionComponentController.StartFlowPreparations(selectedFlow);
    }
    private void creatNewRole(MouseEvent event){
        newRoleName = "";
        newRoleDescription = "";
        selectedRole = new ClientRole(newRoleName, newRoleDescription, new HashSet<>());
        int row = -1;
        clearFlowFullData();
        ItemFullDetails.add(new Label("________________________________"), 0, ++row);
        ItemFullDetails.add(new Label("_______________________________"), 1, row);
        ItemFullDetails.add(new Label("Choose Name:"), 0, ++row);
        TextField roleNameTextField = new TextField();
        roleNameTextField.textProperty().addListener((observable, oldValue, newValue) -> newRoleName = newValue);
        ItemFullDetails.add(roleNameTextField, 1, row);

        ItemFullDetails.add(new Label("________________________________"), 0, ++row);
        ItemFullDetails.add(new Label("_______________________________"), 1, row);
        ItemFullDetails.add(new Label("Role Description:"), 0, ++row);
        TextField roleDescription = new TextField();
        roleDescription.textProperty().addListener((observable, oldValue, newValue) -> newRoleDescription = newValue);
        ItemFullDetails.add(roleDescription, 1, row);

        ItemFullDetails.add(new Label("________________________________"), 0, ++row);
        ItemFullDetails.add(new Label("_______________________________"), 1, row);
        ItemFullDetails.add(new Label("Choose Flows:"), 0, ++row);
        checkBoxList = new ArrayList<>();
        if(flowDefinitionsStorage != null) {
            for (FlowDefinition flow : flowDefinitionsStorage.getFlowDefinitions()) {
                ItemFullDetails.add(new Label("________________________________"), 0, ++row);
                ItemFullDetails.add(new Label("_______________________________"), 1, row);
                CheckBox checkBox = new CheckBox(flow.getName());
                checkBox.setSelected(false);
                ItemFullDetails.add(checkBox, 1, ++row);
                checkBoxList.add(checkBox);
            }
        }
        makeButton(ItemsTypeRegistry.ROLE, ++row);
    }
}
