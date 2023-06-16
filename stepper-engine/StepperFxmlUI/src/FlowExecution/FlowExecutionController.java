package FlowExecution;

import FlowExecution.FlowStepsContainer.FlowStepsContainerController;
import FlowExecution.UserInput.UserInputController;
import Utils.ControllerUtilities;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
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

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
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
    @FXML
    private ChoiceBox<String> flowContinuationChoiceBox;
    private ObservableList<String> flowContinuationOptions;
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
        this.flowExecution = flowExecution;
        flowContinuationOptions = FXCollections.observableArrayList(flowExecution.getFlowDefinition().GetFlowContinuationsFlows());
        flowContinuationChoiceBox.setItems(flowContinuationOptions);
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
        flowContinuationOptions = FXCollections.observableArrayList(flowExecution.getFlowDefinition().GetFlowContinuationsFlows());
        flowContinuationChoiceBox.setItems(flowContinuationOptions);
        Map<String, Object> initialValues = flowDefinition.GetInitialValues();
        Map<String, DataCapsuleImpl> freeInputs = flowDefinition.getFlowFreeInputs();
        for (String inputName : freeInputs.keySet()){
            DataCapsuleImpl inputCapsule = freeInputs.get(inputName);
            String DataType = inputCapsule.getDataDefinitionDeclaration().dataDefinition().getName().toUpperCase();
            String initialValue = initialValues.getOrDefault(inputCapsule.getFinalName(), "").toString();
            if(!initialValue.equals("")){
                inputsCount.getAndIncrement();
            }
            loadInputFxml(inputCapsule.GetUserFriendlyName(),DataDefinitionRegistry.valueOf(DataType),inputCapsule.IsNecessary(),inputCapsule.getFinalName(),initialValue);
        }

    }
    @FXML
    private void initialize() {
        flowStepsContainerComponentController.SetStepExpandEvent(this::ExpandStepForDetails);
        EnableFlowStart = new SimpleBooleanProperty(true);
        startFlowExecutionButton.disableProperty().bind(EnableFlowStart);
        flowContinuationOptions = FXCollections.observableArrayList();
        flowContinuationChoiceBox.setItems(flowContinuationOptions);
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
            addInputWithAnimation(root);
            UserInputController flowController = loader.getController();
            flowController.AddInputToScene(inputName, inputType, necessity,finalName,StartValue);
            startFlowExecutionButton.addEventHandler(ActionEvent.ACTION, event -> flowController.AddListenerToControllerButton(flowExecution));

            if(flowController.addListenerIfMandatory(inputsCount, this::CountTextFieldsFilled)){
                mandatoryInputsCount++;
            }
            //Thread.sleep(100);
        } catch (Exception e) {
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
        aTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            onFinish.run();
        });
    }
    private void UpdateFlowExecutionHeader(List<String> flowExecutionStartData){
        flowStepsContainerComponentController.UpdateFlowExecutionHeader(flowExecutionStartData);
    }
    public void ExpandStepForDetails(String stepName){
        if(flowFullDetails != null) {
            ControllerUtilities.ExpandStepForDetails(stepName, flowDetailsGridPane, flowFullDetails);
        }
    }

    private void FlowDetailsDistribute(FlowFullDetails flowFullDetails){
        this.flowFullDetails = flowFullDetails;
        mainController.AddFlowToHistory(flowFullDetails);
        Pair<List<StatisticData>,List<StatisticData>> statistics = flowFullDetails.GetStatistics();
        mainController.UpdateStatistics(statistics.getKey(), statistics.getValue());
    }
    private void setFinish(){

    }
    @FXML
    void StartFlowExecutionAction(ActionEvent event) {
        flowStepsContainerComponentController.clearStepsData();
        FlowExecutor flowExecutor = new FlowExecutor(flowExecution,this::UpdateFlowExecutionHeader,this::FlowDetailsDistribute, flowStepsContainerComponentController::loadSingleStepFXML);
        //BindTaskToUIComponents(flowExecutor,this::setFinish);
        ExecutorService threadExecutor = mainController.getThreadExecutor();
        threadExecutor.execute(flowExecutor);
    }
    @FXML
    void FlowContinuationButtonClicked(ActionEvent event){
        String choice = flowContinuationChoiceBox.valueProperty().get();
        if(choice!= null && !choice.equals("") && flowFullDetails != null){
            mainController.RunContinuation(flowExecution, choice, flowFullDetails);
        }
    }
    public void rerunFlow(FlowExecution flowExecution){
        StartFlowPreparations(flowExecution);
        inputsCount.set(mandatoryInputsCount);
        CountTextFieldsFilled();
    }
    private void addInputWithAnimation(Node inputRoot) {

        inputRoot.setOpacity(0);

        inputRoot.setTranslateX(FlowInputsContainer.getWidth());

        FlowInputsContainer.getChildren().add(inputRoot);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), inputRoot);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), inputRoot);
        translateTransition.setFromX(FlowInputsContainer.getWidth());
        translateTransition.setToX(0);

        fadeTransition.play();
        translateTransition.play();
    }
}
