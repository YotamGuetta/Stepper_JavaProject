package FlowExecution.FlowStepsContainer;

import FlowExecution.FlowStep.FlowStepController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class FlowStepsContainerController {

    @FXML
    private GridPane flowHeaderGridPane;

    @FXML
    private Text flowNameLabel;

    @FXML
    private Text flowStartTimeLabel;

    @FXML
    private VBox flowExecutionStepContainer;

    @FXML
    private Text flowUniqueIdLabel;

    private Parent lastStepClicked;
    private Consumer<String> ExpandEvent;
    public void SetStepExpandEvent(Consumer<String> ExpandEvent){
        this.ExpandEvent = ExpandEvent;
    }
    public void clearStepsData(){
        flowExecutionStepContainer.getChildren().clear();
        flowExecutionStepContainer.getChildren().add(flowHeaderGridPane);
        clearProperties();
    }
    private void clearProperties(){
        flowNameLabel.setText("");
        flowUniqueIdLabel.setText("");
        flowStartTimeLabel.setText("");
    }

    public void UpdateFlowExecutionHeader(List<String> flowExecutionStartData){
        flowNameLabel.setText(flowExecutionStartData.get(0));
        flowUniqueIdLabel.setText(flowExecutionStartData.get(1));
        flowStartTimeLabel.setText(flowExecutionStartData.get(2));
    }
    public void loadSingleStepFXML(Pair<String,String> stepNameAndResult) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlowExecution/FlowStep/FlowStepSceneLayout.fxml"));
            Parent root = loader.load();
            flowExecutionStepContainer.getChildren().add(root);

            // If the SingleController class is needed for interacting with the contents of single.fxml
            FlowStepController flowController = loader.getController();
            flowController.SetFlowData(stepNameAndResult.getKey(), stepNameAndResult.getValue(), ExpandEvent);
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

}
