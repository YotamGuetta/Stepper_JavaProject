package FlowExecution;

import Flow.FlowController;
import FlowExecution.FlowStep.FlowStepController;
import FlowExecution.UserInput.UserInputController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import stepper.dd.impl.DataDefinitionRegistry;
import stepper.flow.definition.api.FlowDefinition;
import stepper.step.api.DataCapsuleImpl;
import stepper.step.api.DataNecessity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class FlowExecutionController {
    @FXML
    private FlowPane FlowInputsContainer;
    @FXML
    private Button startFlowExecutionButton;
    @FXML
    private Label flowNameLabel;
    @FXML
    private VBox flowExecutionStepContainer;
    @FXML
    private Label flowUniqueIdLabel;

    @FXML
    private Label flowStartTimeLabel;
    private FlowDefinition flowDefinition;
    private AtomicInteger inputsCount;
    private SimpleBooleanProperty EnableFlowStart;
    private int mandatoryInputsCount = 0;

    public void StartFlowPreparations(FlowDefinition flowDefinition){
        FlowInputsContainer.getChildren().clear();
        inputsCount = new AtomicInteger();
        inputsCount.set(0);
        this.flowDefinition = flowDefinition;
        Map<String, DataCapsuleImpl> freeInputs = flowDefinition.getFlowFreeInputs();
        for (String inputName : freeInputs.keySet()){
            DataCapsuleImpl inputCapsule = freeInputs.get(inputName);
            String DataType = inputCapsule.getDataDefinitionDeclaration().dataDefinition().getName().toUpperCase();
            loadInputFxml(inputCapsule.GetUserFriendlyName(),DataDefinitionRegistry.valueOf(DataType),inputCapsule.IsNecessary());
        }

    }
    @FXML
    private void initialize() {
        EnableFlowStart = new SimpleBooleanProperty(true);
        startFlowExecutionButton.disableProperty().bind(EnableFlowStart);
    }
    private void loadInputFxml(String inputName, DataDefinitionRegistry inputType, DataNecessity necessity) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlowExecution/UserInput/InputSceneLayout.fxml"));
            Parent root = loader.load();
            FlowInputsContainer.getChildren().add(root);
            //root.setStyle("/Flow/FlowStyles.css");

            // If the SingleController class is needed for interacting with the contents of single.fxml
            UserInputController flowController = loader.getController();
            flowController.AddInputToScene(inputName, inputType, necessity);

            if(flowController.addListenerIfMandatory(inputsCount, this::CountTextFieldsFilled)){
                mandatoryInputsCount++;
            }
            // Perform any necessary setup or interaction with the SingleController
            // For example: singleController.setData(someData);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }
    }
    private void CountTextFieldsFilled(){
            EnableFlowStart.set(!(mandatoryInputsCount == inputsCount.get()));
    }

    public void BindTaskToUIComponents(Task<Boolean> aTask, Runnable onFinish){

    }
    private void UpdateFlowExecutionHeader(String flowName, String flowUniqueID, String flowStartTime){
        flowNameLabel.setText(flowName);
        flowUniqueIdLabel.setText(flowUniqueID);
        flowStartTimeLabel.setText(flowStartTime);
    }
    public void loadSingleStepFXML(String StepName, String StepResult) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlowExecution/FlowStep/FlowStepSceneLayout.fxml"));
            Parent root = loader.load();
            flowExecutionStepContainer.getChildren().add(root);

            // If the SingleController class is needed for interacting with the contents of single.fxml
            FlowStepController flowController = loader.getController();
            flowController.SetFlowData(StepName, StepResult);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }
    }
}
