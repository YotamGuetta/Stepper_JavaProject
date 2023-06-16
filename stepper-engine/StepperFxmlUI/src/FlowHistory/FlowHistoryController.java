package FlowHistory;

import FlowExecution.FlowStepsContainer.FlowStepsContainerController;
import FlowHistory.FlowBasicDetails.FlowBasicDetailsController;
import Utils.ControllerUtilities;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import stepper.flow.execution.FlowExecution;
import stepper.dataStorage.FlowFullDetails;
import stepper.dataStorage.stepDetails;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class FlowHistoryController{
    private final ObservableList<String> filterOptions = FXCollections
            .observableArrayList("Default", "Only Success", "Only Warnings", "Only Failures");
    private final Map<String, Pair<FlowFullDetails, SimpleBooleanProperty>> flowHistoryData;
    //                 <Uuid, <flow, is visible>>
    private FlowFullDetails currentSelectedFlow;
    private Consumer<FlowExecution> flowRerunAction;
    @FXML
    private FlowStepsContainerController flowStepsContainerComponentController;

    @FXML
    private ChoiceBox<String> historyFilterCheckBox;

    @FXML
    private HBox flowHistoryContainer;

    @FXML
    private VBox flowExecutionDetailsContainer;
    @FXML
    private GridPane flowDetailsGridPaneH;
    @FXML
    private VBox flowHistoryFullDetailsContainer;
    public FlowHistoryController(){
        flowHistoryData  = new HashMap<>();
    }
    @FXML
    private void initialize() {
        flowStepsContainerComponentController.SetStepExpandEvent(this::ExpandStepForDetails);
        historyFilterCheckBox.setItems(filterOptions);
        historyFilterCheckBox.setValue(historyFilterCheckBox.getItems().get(0));
        historyFilterCheckBox.valueProperty().addListener(((observable, oldValue, newValue) -> filterFlows(newValue)));
    }
    public void setFlowRerunAction(Consumer<FlowExecution> flowRerunAction){
        this.flowRerunAction = flowRerunAction;
    }
    public void ExpandStepForDetails(String stepName){
        ControllerUtilities.ExpandStepForDetails(stepName, flowDetailsGridPaneH, currentSelectedFlow);
    }
    private boolean doesPassFilter(String filter, String value){
        return filter.equals(historyFilterCheckBox.getItems().get(0)) || (filter.toUpperCase().contains(value.toUpperCase()));
    }
    private void filterFlows(String newValue){
        for(String uuid: flowHistoryData.keySet()) {
            Pair<FlowFullDetails, SimpleBooleanProperty> currentFlow = flowHistoryData.get(uuid);
            currentFlow.getValue().set(doesPassFilter(newValue, currentFlow.getKey().getFlowResult()));
        }
    }
    @FXML
    void RerunFlowButtonClicked(ActionEvent event) {
        flowRerunAction.accept(currentSelectedFlow.getFlowExecution());
    }
    @FXML
    void FlowHistoryFilterSelected(ContextMenuEvent event) {
        System.out.println("switched");
    }
    private void AddFlowBasicDetails(FlowFullDetails flowFullDetails){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlowHistory/FlowBasicDetails/FlowBasicDetailsSceneLayout.fxml"));
            Parent root = loader.load();
            flowHistoryContainer.getChildren().add(root);

            // If the SingleController class is needed for interacting with the contents of single.fxml
            FlowBasicDetailsController flowController = loader.getController();
            flowController.AddFlowBasicDetails(flowFullDetails.getFlowName(),
                    flowFullDetails.getStartTime(),flowFullDetails.getFlowResult(),flowFullDetails.getUuid());
            flowController.AddMouseClickedListener(this::OnFlowBasicDetailsClicked);
            flowController.AddHideListener(flowHistoryData.get(flowFullDetails.getUuid()).getValue());


        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }
    }
    public void OnFlowBasicDetailsClicked(String flowUuid){
        System.out.println("clicked");
        flowStepsContainerComponentController.clearStepsData();
        currentSelectedFlow = flowHistoryData.get(flowUuid).getKey();
        flowStepsContainerComponentController.UpdateFlowExecutionHeader(
                Arrays.asList(currentSelectedFlow.getFlowName(),currentSelectedFlow.getStartTime(), currentSelectedFlow.getFlowResult()));

        int count =0;
        for(stepDetails fullStepDetails: currentSelectedFlow.getSteps().values()) {
                flowStepsContainerComponentController.loadSingleStepFXML(new Pair<>(fullStepDetails.getName(), fullStepDetails.getResult()));
            count++;
        }
    }
    public void AddNewFlowToHistory(FlowFullDetails flowFullDetails){
        flowHistoryData.put(flowFullDetails.getUuid(),
                new Pair<>(flowFullDetails, new SimpleBooleanProperty(true)));
        AddFlowBasicDetails(flowFullDetails);
        HideIfFiltered(flowFullDetails);
    }
    private void HideIfFiltered(FlowFullDetails flowFullDetails){
        String filter = historyFilterCheckBox.getValue();
        if(!filter.equals(historyFilterCheckBox.getItems().get(0))){
            if(!filter.toUpperCase().contains(flowFullDetails.getFlowResult().toUpperCase())){
                flowHistoryData.get(flowFullDetails.getUuid()).getValue().set(false);
            }
        }
    }
}