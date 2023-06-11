package FlowExecution;

import FlowExecution.FlowStep.FlowStepController;
import FlowExecution.UserInput.UserInputController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;
import stepper.dd.impl.DataDefinitionRegistry;
import stepper.flow.definition.api.FlowDefinition;
import stepper.flow.execution.FlowExecution;
import stepper.flow.execution.FlowFullDetails;
import stepper.flow.execution.runner.FlowExecutor;
import stepper.step.api.DataCapsuleImpl;
import stepper.step.api.DataNecessity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class FlowExecutionController {
    @FXML
    public GridPane flowHeaderGridPane;
    @FXML
    private FlowPane FlowInputsContainer;
    @FXML
    private Button startFlowExecutionButton;
    @FXML
    private Text flowNameLabel;
    @FXML
    private VBox flowExecutionStepContainer;
    @FXML
    private Text flowUniqueIdLabel;
    @FXML
    private GridPane flowDetailsGridPane;
    @FXML
    private Text flowStartTimeLabel;
    @FXML
    private HBox startExecutionButtonContainer;
    private  FlowFullDetails flowFullDetails;
    private  FlowExecution flowExecution;
    private Parent lastStepClicked;
    private AtomicInteger inputsCount;
    private SimpleBooleanProperty EnableFlowStart;
    private int mandatoryInputsCount;
    private  void clearAllFlowData(){
        FlowInputsContainer.getChildren().clear();
        flowExecutionStepContainer.getChildren().clear();
        flowExecutionStepContainer.getChildren().add(flowHeaderGridPane);
        flowDetailsGridPane.getChildren().clear();
        clearProperties();
        clearListeners();
        clearButtonData();
    }
    private  void clearListeners(){
        EnableFlowStart.set(true);
    }
    private void clearProperties(){
        flowNameLabel.setText("");
        flowUniqueIdLabel.setText("");
        flowStartTimeLabel.setText("");
    }
    public void StartFlowPreparations(FlowDefinition flowDefinition){
        clearAllFlowData();
        inputsCount = new AtomicInteger();
        inputsCount.set(0);
        mandatoryInputsCount = 0;
        flowExecution = new FlowExecution(UUID.randomUUID().toString(), flowDefinition);
        Map<String, DataCapsuleImpl> freeInputs = flowDefinition.getFlowFreeInputs();
        for (String inputName : freeInputs.keySet()){
            DataCapsuleImpl inputCapsule = freeInputs.get(inputName);
            String DataType = inputCapsule.getDataDefinitionDeclaration().dataDefinition().getName().toUpperCase();
            loadInputFxml(inputCapsule.GetUserFriendlyName(),DataDefinitionRegistry.valueOf(DataType),inputCapsule.IsNecessary(),inputCapsule.getFinalName());
        }

    }
    @FXML
    private void initialize() {
        EnableFlowStart = new SimpleBooleanProperty(true);
        startFlowExecutionButton.disableProperty().bind(EnableFlowStart);
    }
    private void clearButtonData(){
        Button originalStartFlowExecutionButton = new Button("Start !");
        originalStartFlowExecutionButton.setId(startFlowExecutionButton.getId());
        originalStartFlowExecutionButton.setOnMouseClicked(startFlowExecutionButton.getOnMouseClicked());
        startExecutionButtonContainer.getChildren().remove(startFlowExecutionButton);
        startExecutionButtonContainer.getChildren().add(originalStartFlowExecutionButton);
        startFlowExecutionButton = originalStartFlowExecutionButton;
        startFlowExecutionButton.disableProperty().bind(EnableFlowStart);
        startFlowExecutionButton.setOnAction(this::StartFlowExecutionAction);
    }
    private void loadInputFxml(String inputName, DataDefinitionRegistry inputType, DataNecessity necessity, String finalName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlowExecution/UserInput/InputSceneLayout.fxml"));
            Parent root = loader.load();
            FlowInputsContainer.getChildren().add(root);

            UserInputController flowController = loader.getController();
            flowController.AddInputToScene(inputName, inputType, necessity,finalName);
            startFlowExecutionButton.addEventHandler(ActionEvent.ACTION, event -> flowController.AddListenerToControllerButton(flowExecution));

            if(flowController.addListenerIfMandatory(inputsCount, this::CountTextFieldsFilled)){
                mandatoryInputsCount++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void CountTextFieldsFilled(){
            EnableFlowStart.set(!(mandatoryInputsCount == inputsCount.get()));
    }

    public void BindTaskToUIComponents(Task<Boolean> aTask, Runnable onFinish){
       // stepName.bind(aTask.titleProperty());
        //aTask.messageProperty().addListener((observable, oldValue, newValue) -> {
        //    loadSingleStepFXML(newValue);
        //});
        aTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            onFinish.run();
        });
    }
    private void UpdateFlowExecutionHeader(List<String> flowExecutionStartData){
        flowNameLabel.setText(flowExecutionStartData.get(0));
        flowUniqueIdLabel.setText(flowExecutionStartData.get(1));
        flowStartTimeLabel.setText(flowExecutionStartData.get(2));
    }
    public void ExpandStepForDetails(String stepName){
        flowDetailsGridPane.getChildren().clear();
        Map<String,String> stepDetails = flowFullDetails.getAStepDetails(stepName);
        int index = 0;
        for(String name : stepDetails.keySet()){
            flowDetailsGridPane.add(new Label(name+":"), 0, index);
            flowDetailsGridPane.add(new Label(stepDetails.get(name)), 1, index);
            index++;
        }
    }
    public void loadSingleStepFXML(Pair<String,String> stepNameAndResult) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlowExecution/FlowStep/FlowStepSceneLayout.fxml"));
            Parent root = loader.load();
            flowExecutionStepContainer.getChildren().add(root);

            // If the SingleController class is needed for interacting with the contents of single.fxml
            FlowStepController flowController = loader.getController();
            flowController.SetFlowData(stepNameAndResult.getKey(), stepNameAndResult.getValue(), this);
            //flowController.AddListenerIfClicked(this::RunWhenStepIsClicked);


        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }

    }
    private void RunWhenStepIsClicked(Parent flowClicked){
        if(lastStepClicked !=null){
            lastStepClicked.setDisable(false);
        }
        lastStepClicked = flowClicked;
        flowClicked.setDisable(true);
    }
    private void FlowDetailsDistribute(FlowFullDetails flowFullDetails){
        this.flowFullDetails = flowFullDetails;
        System.out.println("got flow details");
    }
    private void setFinish(){
        System.out.println("task Finished");
    }
    @FXML
    void StartFlowExecutionAction(ActionEvent event) {
        FlowExecutor flowExecutor = new FlowExecutor(flowExecution,this::UpdateFlowExecutionHeader,this::FlowDetailsDistribute, this::loadSingleStepFXML);
        //BindTaskToUIComponents(flowExecutor,this::setFinish);
        new Thread(flowExecutor).start();
    }

}
