package FlowExecution.FlowStep;

import FlowExecution.FlowExecutionController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.function.Consumer;

public class FlowStepController {
    @FXML
    private StackPane stepContainerPane;
    @FXML
    private Label stepNameLabel;
    @FXML
    private Label stepResultLabel;
    @FXML
    private GridPane stepDataGridPane;
    private FlowExecutionController flowExecutionController;

    public void SetFlowData(String stepName, String stepResult, FlowExecutionController flowExecutionController){
        stepNameLabel.setText(stepName);
        stepResultLabel.setText(stepResult);
        this.flowExecutionController = flowExecutionController;
    }
    public void AddListenerIfClicked(Consumer<Parent> stepClickedDelegate){
       // stepDataGridPane.onMouseClickedProperty().addListener((observable, oldValue, newValue) -> {
       //     stepClickedDelegate.accept(stepDataGridPane);
       // });
    }
    @FXML
    void StepWasClicked(MouseEvent event) {
        flowExecutionController.ExpandStepForDetails(stepNameLabel.getText());
    }
}
