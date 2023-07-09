package FlowHistory.FlowBasicDetails;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.function.Consumer;

public class FlowBasicDetailsController {

    private String flowRunUniqueId;

    @FXML
    private StackPane FlowBasicDetailsPane;

    @FXML
    private GridPane stepDataGridPane;

    @FXML
    private Label flowNameLabel;

    @FXML
    private Label flowStartTimeLabel;

    @FXML
    private Label flowResultLabel;

    public void AddFlowBasicDetails(String flowName, String flowTime, String flowResult, String flowRunUniqueId){
        flowNameLabel.setText(flowName);
        flowStartTimeLabel.setText(flowTime);
        flowResultLabel.setText(flowResult);
        this.flowRunUniqueId = flowRunUniqueId;
    }
    public void AddMouseClickedListener(Consumer<String> expandDetails) {
        FlowBasicDetailsPane.setOnMouseClicked(event -> expandDetails.accept(flowRunUniqueId));
    }
    public void AddHideListener(SimpleBooleanProperty hideFlowDetails){
        FlowBasicDetailsPane.visibleProperty().bind(hideFlowDetails);
        FlowBasicDetailsPane.managedProperty().bind(hideFlowDetails);
    }
}