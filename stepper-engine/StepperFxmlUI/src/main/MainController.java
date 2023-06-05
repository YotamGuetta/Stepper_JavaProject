package main;


import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import stepper.flow.definition.api.FlowDefinition;
import stepper.flow.definition.api.ReadFlowFromFile;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class MainController {
    private File lastDirectory;
    private Stage primaryStage;
    private final SimpleStringProperty selectedFileProperty;
    private SimpleStringProperty fileErrorMessage;
    @FXML
    private ImageView StepperTitle;
    @FXML
    private Button LoadfileButton;
    @FXML
    private Label ErrorFileLoadedLable;
    @FXML
    private TextField LoadFileTextField;

    public MainController(){
        selectedFileProperty = new SimpleStringProperty();
        fileErrorMessage = new SimpleStringProperty();
    }

    @FXML
    private void initialize() {
        LoadFileTextField.setDisable(true);
        LoadFileTextField.textProperty().bind(selectedFileProperty);
        ErrorFileLoadedLable.textProperty().bind(fileErrorMessage);
    }

    public void SetPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    void OpenFileButtonAction(ActionEvent event) {
        File selectedFile;
        FileChooser fileChooser = new FileChooser();
        if (lastDirectory != null) {
            fileChooser.setInitialDirectory(lastDirectory);
        }
        fileChooser.setTitle("Select flows file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));

        selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }
        //Save directory
        lastDirectory = selectedFile.getParentFile();

        String absolutePath = selectedFile.getAbsolutePath();
        selectedFileProperty.set(absolutePath);

        ReadFlowFromFile readFlowFromFile = new ReadFlowFromFile();



            Consumer<List<FlowDefinition>> data = (d)-> {
                try {
                    readFlowFromFile.getFlowDefinitions(absolutePath);
                    fileErrorMessage.set("");
                }
                catch (InvalidPropertiesFormatException e){
                    fileErrorMessage.set("Error: "+ e.getMessage());
                }
                    catch (IOException | JAXBException e) {
                        fileErrorMessage.set("Error: "+ e.getMessage());
                }
            };


        //isFileSelected.set(true);

    }


}

