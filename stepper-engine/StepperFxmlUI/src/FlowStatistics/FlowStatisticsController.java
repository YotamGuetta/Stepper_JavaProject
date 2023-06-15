package FlowStatistics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import stepper.dataStorage.StatisticData;

import java.util.List;

public class FlowStatisticsController {

    @FXML
    private TableColumn<StatisticData, Integer> stepsUsersColumn;

    @FXML
    private TableColumn<StatisticData, Integer> flowUsersColumn;

    @FXML
    private TableColumn<StatisticData, Double> stepsAverageTimeColumn;

    @FXML
    private TableColumn<StatisticData, String> flowsColumn;

    @FXML
    private TableColumn<StatisticData, String> stepsColumn;

    @FXML
    private TableView<StatisticData> flowsTableView;

    @FXML
    private TableColumn<StatisticData, Double> flowsAverageTimeColumn;

    @FXML
    private TableView<StatisticData> stepsTableView;

    ObservableList<StatisticData> StepsList = FXCollections.observableArrayList();
    ObservableList<StatisticData> FlowsList = FXCollections.observableArrayList();


    @FXML
    private void initialize() {
        stepsUsersColumn.setCellValueFactory(new PropertyValueFactory<>("timeUsed"));
        flowUsersColumn.setCellValueFactory(new PropertyValueFactory<>("timeUsed"));
        stepsAverageTimeColumn.setCellValueFactory(new PropertyValueFactory<>("AverageRunTime"));
        flowsAverageTimeColumn.setCellValueFactory(new PropertyValueFactory<>("AverageRunTime"));
        flowsColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        stepsColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        stepsTableView.setItems(StepsList);
        flowsTableView.setItems(FlowsList);

    }
    public void UpdateStatistics(List<StatisticData> flows, List<StatisticData> Steps){
        for(StatisticData data : flows){
            int index = FlowsList.indexOf(data);
            if(index >= 0){
                FlowsList.set(index,FlowsList.get(index).AddAdditionalVariables(data));
            }
            else{
                FlowsList.add(data);
            }
        }
        for(StatisticData data : Steps){
            int index = StepsList.indexOf(data);
            if(index >= 0){
                StepsList.set(index,StepsList.get(index).AddAdditionalVariables(data));
            }
            else{
                StepsList.add(data);
            }
        }
    }
}