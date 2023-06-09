package FlowExecution.UserInput;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import stepper.dd.impl.DataDefinitionRegistry;
import stepper.step.api.DataNecessity;

import java.util.concurrent.atomic.AtomicInteger;

public class UserInputController {
    private DataNecessity inputNecessity;
    @FXML
    private Label InputNameLabel;
    @FXML
    private  Label necessityTextLabel;
    @FXML
    private TextField InputValueTextField;
/////////////////////////////////////////////////////////////////////////
    public boolean addListenerIfMandatory(AtomicInteger filledFieldsCount, Runnable CountTextFilled){
        if(inputNecessity.equals(DataNecessity.MANDATORY)) {
            InputValueTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.isEmpty() && !oldValue.isEmpty()) {
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
////////////////////////////////////////////////////////////////////////
    public void AddInputToScene(String inputName, DataDefinitionRegistry inputType, DataNecessity necessity){
        inputNecessity = necessity;
        InputNameLabel.setText(inputName);
        InputValueTextField.setPromptText(inputType.getName());
        switch (inputType){
            case NUMBER: InputValueTextField.setOnKeyPressed(this::NumberTextField);
                break;
            case DOUBLE: InputValueTextField.setOnKeyPressed(this::DoubleTextField);
                break;
        }
        if(!necessity.equals(DataNecessity.MANDATORY)){
            necessityTextLabel.setStyle("-fx-text-fill: Green");
            necessityTextLabel.setText("("+necessity+")");
        }
    }

    private void NumberTextField(KeyEvent keyEvent) {
        checkIfValidNumber(keyEvent, "-");
    }
    private void checkIfValidNumber (KeyEvent keyEvent, String validSigns){
        String input = keyEvent.getCharacter();
        try {
            Integer.parseInt(input);
            keyEvent.consume();
        }
        catch (Exception e){
            if(validSigns.contains(input)){
                keyEvent.consume();
            }
        }
    }
    private void DoubleTextField(KeyEvent keyEvent){
        checkIfValidNumber(keyEvent, "-.");
    }
    public void registerTextFieldListener(){

    }
    public boolean HasInputValue(){
        return !InputValueTextField.getText().isEmpty();
    }

}
