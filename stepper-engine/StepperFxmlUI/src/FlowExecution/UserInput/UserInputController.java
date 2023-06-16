package FlowExecution.UserInput;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.Pair;
import stepper.dd.impl.DataDefinitionRegistry;
import stepper.dd.impl.enumerator.EnumeratorData;
import stepper.flow.execution.FlowExecution;
import stepper.step.api.DataNecessity;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class UserInputController {
    private DataNecessity inputNecessity;
    @FXML
    private Label InputNameLabel;
    @FXML
    private  Label necessityTextLabel;
    @FXML
    private TextField InputValueTextField;
    @FXML
    private HBox inputContinerHBox;
    private ChoiceBox<String> inputValueChoiceBox;
    private DataDefinitionRegistry thisInputType;
    private  String finalName;
/////////////////////////////////////////////////////////////////////////
    public boolean isNumber(String str) {
    try {
        Double.parseDouble(str);
        return true;
    } catch (NumberFormatException e) {
        return false;
    }
}
    public boolean IsNumericValue(){
        return thisInputType.equals(DataDefinitionRegistry.NUMBER) || thisInputType.equals(DataDefinitionRegistry.DOUBLE);
    }
    public boolean addListenerIfMandatory(AtomicInteger filledFieldsCount, Runnable CountTextFilled){
        if(inputNecessity.equals(DataNecessity.MANDATORY)) {
            InputValueTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                if(IsNumericValue()){
                    if((newValue.isEmpty() || newValue.equals("-")) && isNumber(oldValue)){
                        filledFieldsCount.getAndDecrement();
                    }
                    else if(isNumber(newValue) && (oldValue.isEmpty() || oldValue.equals("-"))){
                        filledFieldsCount.getAndIncrement();
                    }
                }
                else if (newValue.isEmpty() && !oldValue.isEmpty()) {
                    filledFieldsCount.getAndDecrement();
                } else if (!newValue.isEmpty() && oldValue.isEmpty()) {
                    filledFieldsCount.getAndIncrement();
                }
                CountTextFilled.run();
            });
        }
        else {
            return false;
        }
        return true;
    }
    public void AddListenerToControllerButton(FlowExecution flowExecution){
        if(!InputValueTextField.getText().isEmpty() ){
            Pair<String ,Object> inputToAdd = GetFreeInputData();
            flowExecution.addFreeInput(inputToAdd.getKey(), inputToAdd.getValue());
            //System.out.println(inputToAdd.getKey()+", Has :"+inputToAdd.getValue());
        }
    }
    public Pair<String ,Object> GetFreeInputData(){
        Pair<String ,Object> inputData;
        switch (thisInputType){
            case NUMBER: inputData = new Pair<>(finalName,Integer.parseInt(InputValueTextField.getText()));
                break;
            case DOUBLE: inputData = new Pair<>(finalName,Double.parseDouble(InputValueTextField.getText()));
                break;
            case  ENUMERATOR:EnumeratorData enumeratorData = new EnumeratorData();
                enumeratorData.setCurrentValue(inputValueChoiceBox.getValue());
                inputData = new Pair<>(finalName, enumeratorData);
                break;
            default: inputData = new Pair<>(finalName,InputValueTextField.getText());
        }
        return inputData;
    }
////////////////////////////////////////////////////////////////////////
    public void AddInputToScene(String inputName, DataDefinitionRegistry inputType, DataNecessity necessity, String finalName, String startValue){
        inputNecessity = necessity;
        thisInputType = inputType;
        this.finalName = finalName;
        InputNameLabel.setText(inputName);
        InputValueTextField.setPromptText(inputType.getName());
        switch (inputType){
            case NUMBER: InputValueTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null && !newValue.isEmpty()) {
                        if (!newValue.matches("-?\\d*")) {
                            InputValueTextField.setText(oldValue);
                        }
                    }
                });
                break;
            case DOUBLE: InputValueTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null && !newValue.isEmpty()) {
                    if (!newValue.matches("-?\\d*(\\.\\d*)?")) {
                        InputValueTextField.setText(oldValue);
                    }
                }
            });
            case ENUMERATOR:
                ObservableList<String> inputValueChoices = FXCollections.observableArrayList();
                inputValueChoices.addAll(Arrays.asList(EnumeratorData.values));
                inputValueChoiceBox = new ChoiceBox<>();
                inputValueChoiceBox.setItems(inputValueChoices);
                inputContinerHBox.getChildren().remove(InputValueTextField);
                inputContinerHBox.getChildren().add(inputValueChoiceBox);
                if(inputValueChoices.contains(startValue)){
                    inputValueChoiceBox.setValue(startValue);
                }
                break;
        }
        if(!necessity.equals(DataNecessity.MANDATORY)){
            necessityTextLabel.setStyle("-fx-text-fill: Green");
            necessityTextLabel.setText("("+necessity+")");
        }
        InputValueTextField.setText(startValue);
    }


}
