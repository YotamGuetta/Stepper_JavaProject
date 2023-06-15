package FlowExecution;

import FlowExecution.FlowStepsContainer.FlowStepsContainerController;
import FlowExecution.UserInput.UserInputController;
import Utils.ControllerUtilities;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Pair;
import main.MainController;
import stepper.dd.impl.DataDefinitionRegistry;
import stepper.flow.definition.api.FlowDefinition;
import stepper.flow.execution.FlowExecution;
import stepper.dataStorage.FlowFullDetails;
import stepper.dataStorage.StatisticData;
import stepper.flow.execution.runner.FlowExecutor;
import stepper.step.api.DataCapsuleImpl;
import stepper.step.api.DataNecessity;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class FlowExecutionController {
    @FXML
    private FlowPane FlowInputsContainer;
    @FXML
    private Button startFlowExecutionButton;
    @FXML
    private GridPane flowDetailsGridPane;
    @FXML
    private HBox startExecutionButtonContainer;
    @FXML
    private FlowStepsContainerController flowStepsContainerComponentController;
    private  FlowFullDetails flowFullDetails;
    private  FlowExecution flowExecution;
    private Parent lastStepClicked;
    private AtomicInteger inputsCount;
    private SimpleBooleanProperty EnableFlowStart;
    private int mandatoryInputsCount;
    private MainController mainController;
    public void SetMainController(MainController mainController){
        this.mainController = mainController;
    }
    private  void clearAllFlowData(){
        flowStepsContainerComponentController.clearStepsData();
        FlowInputsContainer.getChildren().clear();
        flowDetailsGridPane.getChildren().clear();
        clearListeners();
        clearButtonData();
    }
    private  void clearListeners(){
        EnableFlowStart.set(true);
    }
    public void StartFlowPreparations(FlowExecution flowExecution){
        clearAllFlowData();
        inputsCount = new AtomicInteger();
        inputsCount.set(0);
        mandatoryInputsCount = 0;
        this.flowExecution =flowExecution;
        Map<String, DataCapsuleImpl> freeInputs = flowExecution.getFlowDefinition().getFlowFreeInputs();
        Map<String, Object> freeInputsValues = flowExecution.getFreeInputs();
        for (String inputName : freeInputs.keySet()){
            DataCapsuleImpl inputCapsule = freeInputs.get(inputName);
            String DataType = inputCapsule.getDataDefinitionDeclaration().dataDefinition().getName().toUpperCase();
            Object inputValue = freeInputsValues.get(inputName);
            loadInputFxml(inputCapsule.GetUserFriendlyName(),
                    DataDefinitionRegistry.valueOf(DataType),inputCapsule.IsNecessary(),
                    inputCapsule.getFinalName(), (inputValue != null) ? inputValue.toString() : "");
        }
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
        flowStepsContainerComponentController.SetStepExpandEvent(this::ExpandStepForDetails);
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
    private void loadInputFxml(String inputName, DataDefinitionRegistry inputType, DataNecessity necessity, String finalName, String StartValue){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlowExecution/UserInput/InputSceneLayout.fxml"));
            Parent root = loader.load();
            FlowInputsContainer.getChildren().add(root);

            UserInputController flowController = loader.getController();
            flowController.AddInputToScene(inputName, inputType, necessity,finalName,StartValue);
            startFlowExecutionButton.addEventHandler(ActionEvent.ACTION, event -> flowController.AddListenerToControllerButton(flowExecution));

            if(flowController.addListenerIfMandatory(inputsCount, this::CountTextFieldsFilled)){
                mandatoryInputsCount++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadInputFxml(String inputName, DataDefinitionRegistry inputType, DataNecessity necessity, String finalName) {
        loadInputFxml(inputName,  inputType,  necessity,  finalName, "");
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
        flowStepsContainerComponentController.UpdateFlowExecutionHeader(flowExecutionStartData);
    }
    public void ExpandStepForDetails(String stepName){
        ControllerUtilities.ExpandStepForDetails(stepName, flowDetailsGridPane, flowFullDetails);
    }



    private void FlowDetailsDistribute(FlowFullDetails flowFullDetails){
        this.flowFullDetails = flowFullDetails;
        mainController.AddFlowToHistory(flowFullDetails);
        Pair<List<StatisticData>,List<StatisticData>> statistics = flowFullDetails.GetStatistics();
        mainController.UpdateStatistics(statistics.getKey(), statistics.getValue());
        System.out.println("got flow details");
    }
    private void setFinish(){
        System.out.println("task Finished");

    }
    @FXML
    void StartFlowExecutionAction(ActionEvent event) {
        flowStepsContainerComponentController.clearStepsData();
        FlowExecutor flowExecutor = new FlowExecutor(flowExecution,this::UpdateFlowExecutionHeader,this::FlowDetailsDistribute, flowStepsContainerComponentController::loadSingleStepFXML);
        //BindTaskToUIComponents(flowExecutor,this::setFinish);
        new Thread(flowExecutor).start();
    }
    public void rerunFlow(FlowExecution flowExecution){
        StartFlowPreparations(flowExecution);
        inputsCount.set(mandatoryInputsCount);
        CountTextFieldsFilled();
    }

}
