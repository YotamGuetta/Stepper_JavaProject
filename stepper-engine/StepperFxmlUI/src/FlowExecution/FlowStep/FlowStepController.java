package FlowExecution.FlowStep;

import javafx.fxml.FXML;
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
    private  Consumer<String> ExpandEvent;

    public void SetFlowData(String stepName, String stepResult, Consumer<String> ExpandEvent){
        stepNameLabel.setText(stepName);
        stepResultLabel.setText(stepResult);
        this.ExpandEvent = ExpandEvent;
    }
    @FXML
    void StepWasClicked(MouseEvent event) {
        ExpandEvent.accept(stepNameLabel.getText());
    }
}
