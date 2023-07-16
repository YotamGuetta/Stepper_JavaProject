package AdministratorUI.main;


import AdministratorUI.FlowStatistics.FlowStatisticsController;
import ClientUI.Flow.FlowController;
import FlowHistory.FlowHistoryController;
import ItemsContainer.ItemsController;
import ItemsContainer.ItemsTypeRegistry;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import stepper.dataStorage.ClientData;
import stepper.dataStorage.FlowDefinitionsStorage;
import stepper.dataStorage.FlowFullDetails;
import stepper.dataStorage.StatisticData;
import stepper.flow.Tasks.CollectFlowsDataTask;
import stepper.flow.definition.api.FlowDefinition;
import stepper.flow.definition.api.ReadFlowFromFile;
import stepper.flow.execution.FlowExecution;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class MainAdminController {
    private ClientData clientData;
    private final FlowDefinitionsStorage flowDefinitionsStorage;
    private ExecutorService threadExecutor;
    private File lastDirectory;
    private Stage primaryStage;
    private final SimpleStringProperty selectedFileProperty;
    private final SimpleStringProperty fileErrorMessage;
    private int tabSelected;
    private FlowDefinition selectedFlow;
    private final ObservableList<String> cssSkinsAvailable = FXCollections.observableArrayList("DIABLO","LEGO");
    private final String[] cssFilePath = {"main/MainStylesDiablo.css", "main/MainStylesLego.css"};
    @FXML
    private  VBox mainStepperContainer;
    @FXML
    private ItemsController rolesComponentController;
    @FXML
    private FlowHistoryController flowHistoryComponentController;
    @FXML
    private FlowStatisticsController flowStatisticsComponentController;
    @FXML
    private ItemsController usersComponentController;
    @FXML
    private TextFlow ErrorTextFlow;
    @FXML
    private Text ErrorFileLoadedText;
    @FXML
    private TabPane flowTabPane;
    @FXML
    private ImageView StepperTitle;
    @FXML
    private Button LoadfileButton;
    @FXML
    private VBox usersContainer;
    @FXML
    private TextField LoadFileTextField;
    @FXML
    private  ChoiceBox<String> CssSkinChoiceBox;
    public MainAdminController(){
        selectedFileProperty = new SimpleStringProperty();
        fileErrorMessage = new SimpleStringProperty();
        clientData = new ClientData();
        flowDefinitionsStorage= new FlowDefinitionsStorage(clientData);
        tabSelected = 0;
    }

    @FXML
    private void initialize() {
        LoadFileTextField.setDisable(true);
        LoadFileTextField.textProperty().bind(selectedFileProperty);
        ErrorFileLoadedText.textProperty().bind(fileErrorMessage);
        //flowExecutionComponentController.SetMainController(this);----------------------------------------
        usersComponentController.SetItemsType(ItemsTypeRegistry.USER);
        usersComponentController.ShowItems();
        rolesComponentController.SetItemsType(ItemsTypeRegistry.ROLE);
        rolesComponentController.SetClientData(clientData);
        rolesComponentController.SetFlowDefinitionsStorage(flowDefinitionsStorage);
        rolesComponentController.ShowItems();
        flowHistoryComponentController.setFlowRerunAction(this::rerunFlow);
        CssSkinChoiceBox.setItems(cssSkinsAvailable);
        CssSkinChoiceBox.setValue(cssSkinsAvailable.get(0));
        CssSkinChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> CssSkinSelectedChanged());
    }
    private void rerunFlow(FlowExecution flowExecution){
        tabSelected = 1;
        flowTabPane.getSelectionModel().select(tabSelected);
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


        Consumer<FlowDefinition> LoadFlowsDelegate = this::saveFlowDefinition;
        Consumer<Exception> OnInvalidFileDelegate = (errorMessage) ->{
            fileErrorMessage.set("Error: "+ errorMessage.getMessage());
        };
        CollectFlowsDataTask collectFlowsDataTask = new CollectFlowsDataTask(absolutePath, LoadFlowsDelegate, OnInvalidFileDelegate, this::OnSuccess);
        collectFlowsDataTask.run();
    }
    private void saveFlowDefinition(FlowDefinition flowDefinition){
        flowDefinitionsStorage.AddFlowDefinition(flowDefinition);
    }
    public void DisableFlow(FlowController flowController){

    }
    public ExecutorService getThreadExecutor(){
        return  threadExecutor;
    }
    public void OnSuccess(ExecutorService threadExecutor){
        if(this.threadExecutor != null){
            this.threadExecutor.shutdown();
        }
        this.threadExecutor = threadExecutor;
        fileErrorMessage.set("");
    }
    public void loadSingleFXML(FlowDefinition flowDefinition) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientUI/Flow/FlowSceneLayout.fxml"));
            Parent root = loader.load();
            usersContainer.getChildren().add(root);
            //root.setStyle("/Flow/FlowStyles.css");

            // If the SingleController class is needed for interacting with the contents of single.fxml
            FlowController flowController = loader.getController();
            flowController.LoadNewFlow(flowDefinition);
            ////flowController.setMainController(this);--------------------------------------
            // Perform any necessary setup or interaction with the SingleController
            flowDefinitionsStorage.AddFlowDefinition(flowDefinition);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }
    }

        @FXML
        private  void StartFlow(MouseEvent event){
            tabSelected = 1;
            flowTabPane.getSelectionModel().select(tabSelected);
        }

    private  void CssSkinSelectedChanged(){
        String choice = CssSkinChoiceBox.getValue();
        primaryStage.getScene().getStylesheets().clear(); // Clear existing stylesheets
        if(choice.equals(cssSkinsAvailable.get(1))) {
            primaryStage.getScene().getStylesheets().add(cssFilePath[1]);
        }
        else{
            primaryStage.getScene().getStylesheets().add(cssFilePath[0]);
        }
    }

        public void AddFlowToHistory(FlowFullDetails flowFullDetails){

            flowHistoryComponentController.AddNewFlowToHistory(flowFullDetails);
        }
        public void UpdateStatistics(List<StatisticData> flows, List<StatisticData> steps){
            flowStatisticsComponentController.UpdateStatistics(flows, steps);
        }
        public void RunContinuation(FlowExecution source, String target, FlowFullDetails flowFullDetails){
            FlowExecution newFlowExecution = source.GetFlowForContinuation(target,flowDefinitionsStorage, flowFullDetails );
        }
}

