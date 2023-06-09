package Flow;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import main.MainController;
import stepper.flow.definition.api.FlowDefinition;

public class FlowController {
    private  FlowDefinition flowDefinition;
    private MainController mainController;
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
        SetFlowData(flowDefinition.getName(), flowDefinition.getDescription(), flowDefinition.getFlowSteps().size(),flowDefinition.getFlowFreeInputs().size(), "");

    }
    private void SetFlowData(String flowName, String flowDescription, Integer StepCount, Integer inputCount, String continuation){
        flowNameLabel.setText(flowName);
        descriptionLabel.setText(flowDescription);
        stepCountLabel.setText(StepCount.toString());
        inputsCountLabel.setText(inputCount.toString());
        ContinuationLabel.setText(continuation);
    }
    public  void setMainController(MainController mainController){

        this.mainController = mainController;
    }
    @FXML
    void FlowDetailsButton(MouseEvent event) {
        opacity.set(1);
        mainController.ShowFlowFullDetails(flowDefinition);
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
