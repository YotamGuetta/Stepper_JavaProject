package Utils;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import stepper.dataStorage.FlowFullDetails;

import java.util.List;
import java.util.Map;

public class ControllerUtilities {
    public static void ExpandStepForDetails(String stepName, GridPane flowDetailsGridPane, FlowFullDetails flowFullDetails) {
        flowDetailsGridPane.getChildren().clear();
        GridPane.setHgrow(flowDetailsGridPane, Priority.ALWAYS);
        GridPane.setVgrow(flowDetailsGridPane, Priority.ALWAYS);
        List<Map<String,Object>> stepFullDetails = flowFullDetails.getAStepDetails(stepName);
        int index = 0;
        index = AddDetailsToGrid(flowDetailsGridPane, stepFullDetails.get(0), index);
        AddValueToGrid(flowDetailsGridPane, index++, 0, "");
        AddValueToGrid(flowDetailsGridPane, index++, 0, "inputs:");
        index = AddDetailsToGrid(flowDetailsGridPane, stepFullDetails.get(1), index);
        AddValueToGrid(flowDetailsGridPane, index++, 0, "");
        AddValueToGrid(flowDetailsGridPane, index++, 0, "outputs:");
        index = AddDetailsToGrid(flowDetailsGridPane, stepFullDetails.get(2), index);
    }

    private static int AddDetailsToGrid(GridPane flowDetailsGridPane, Map<String, Object> stepDetails, int index) {
        for(String name : stepDetails.keySet()){

            AddValueToGrid(flowDetailsGridPane, index, 0, name+":");
            if(stepDetails.get(name) == null){
                AddValueToGrid(flowDetailsGridPane, index, 1, "(No Value)");
            }
            else{
                AddValueToGrid(flowDetailsGridPane, index, 1, stepDetails.get(name).toString());
            }

            index++;
        }
        return index;
    }
    private static void AddValueToGrid(GridPane flowDetailsGridPane, int row,int col, String text){
        TextFlow nameFlow = new TextFlow();
        Text textName = new Text(text);
        textName.getStyleClass().add("text-flow-text");
        nameFlow.getChildren().add(textName);
        flowDetailsGridPane.add(nameFlow, col, row);
    }
}
