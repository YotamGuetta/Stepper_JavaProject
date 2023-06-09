package FlowExecution.FlowStep;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.function.Consumer;

public class FlowStepController {

    @FXML
    private Label stepNameLabel;

    @FXML
    private Label stepResultLabel;
    @FXML
    private GridPane stepDataGridPane;

    public void SetFlowData(String stepName, String stepResult){
        stepNameLabel.setText(stepName);
        stepResultLabel.setText(stepResult);
    }
    public void AddListenerIfClicked(Consumer<FlowStepController> stepClickedDelegate){
        stepDataGridPane.onMouseClickedProperty().addListener((observable, oldValue, newValue) -> {
            stepClickedDelegate.accept(this);
        });
    }
}
