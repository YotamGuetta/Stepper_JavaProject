package ClientUI.Flow;


import ItemsContainer.ItemsController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import main.MainController;
import stepper.clients.ClientRole;
import stepper.clients.ClientUser;
import stepper.flow.definition.api.FlowDefinition;

import java.util.Set;

public class FlowController {
    private  ClientRole clientRole;
    private  ClientUser clientUser;
    private  FlowDefinition flowDefinition;
    private MainController mainController;
    private ItemsController itemsController;
    private Glow glow;
    private SimpleDoubleProperty opacity;
    ObjectProperty<Effect> glowProperty;
    @FXML
    private GridPane FlowSummeryGrid;
    @FXML
    private Text flowNameLabel;

    @FXML
    private Text descriptionLabel;

    @FXML
    private Text stepCountLabel;

    @FXML
    private Text inputsCountLabel;

    @FXML
    private Text ContinuationLabel;

    @FXML
    private void initialize(){
        opacity = new SimpleDoubleProperty(FlowSummeryGrid.getOpacity());
        FlowSummeryGrid.opacityProperty().bind(opacity);
    }
    public void LoadNewFlow(FlowDefinition flowDefinition){
        this.flowDefinition = flowDefinition;
        SetFlowData(flowDefinition.getName(), flowDefinition.getDescription(), flowDefinition.getFlowSteps().size(),flowDefinition.getFlowFreeInputs().size(), flowDefinition.GetFlowContinuationsFlows());

    }
    public void LoadNewRole(ClientRole clientRole){
        this.clientRole = clientRole;
        SetDataName(clientRole.getName());

    }
    public void LoadNewUser(ClientUser clientUser){
        this.clientUser = clientUser;
        SetDataName(clientUser.getName());

    }
    private void SetFlowData(String flowName, String flowDescription, Integer StepCount, Integer inputCount, Set<String> continuation){
        flowNameLabel.setText(flowName);
        descriptionLabel.setText(flowDescription);
        stepCountLabel.setText(StepCount.toString());
        inputsCountLabel.setText(inputCount.toString());
        StringBuilder continuationsList = new StringBuilder();
        for(String continuationFlowName : continuation){
            continuationsList.append(continuationFlowName).append("\n");
        }
        ContinuationLabel.setText(continuationsList.toString());
    }
    private void SetDataName(String Name){
        flowNameLabel.setText(Name);
    }
    public  void setMainController(MainController mainController){
        this.mainController = mainController;
    }

    public  void setItemsController(ItemsController itemsController){
        this.itemsController = itemsController;
    }
    @FXML
    void FlowDetailsButton(MouseEvent event) {
        opacity.set(1);
        if(flowDefinition != null) {
            mainController.ShowFlowFullDetails(flowDefinition);
        }
        else if(clientUser != null){
            itemsController.ShowUserFullDetails(clientUser);
        }
        else {
            itemsController.ShowRoleFullDetails(clientRole);
        }
    }
    @FXML
    void MouseClickedEvent(MouseEvent event) {
        opacity.set(0.8);
    }

    @FXML
    void MouseHoverEvent(MouseEvent event) {
    }
    @FXML
    void MouseStoppedHoverEvent(MouseEvent event) {
    }
}
