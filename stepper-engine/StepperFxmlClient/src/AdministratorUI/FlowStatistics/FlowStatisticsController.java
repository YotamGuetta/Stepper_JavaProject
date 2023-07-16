package AdministratorUI.FlowStatistics;

import javafx.scene.chart.LineChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import stepper.dataStorage.StatisticData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @FXML
    private LineChart<?, ?> stepsLineChart;
    @FXML
    private LineChart<?, ?> flowLineChart;
    private Map<String, XYChart.Series> flowChartLines;
    private Map<String, XYChart.Series> stepsChartLines;

    private ObservableList<StatisticData> StepsList = FXCollections.observableArrayList();
    private ObservableList<StatisticData> FlowsList = FXCollections.observableArrayList();


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
        flowChartLines = new HashMap<>();
        stepsChartLines = new HashMap<>();
    }
    public void UpdateStatistics(List<StatisticData> flows, List<StatisticData> Steps){
        addToChart(flows,Steps);
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
    private void addToChart(List<StatisticData> flows, List<StatisticData> Steps){
        for(StatisticData data : flows){
            int index = FlowsList.indexOf(data);
            addStatsToChart(data, index, flowChartLines, flowLineChart);
        }
        for(StatisticData data : Steps){
            int index = StepsList.indexOf(data);
            addStatsToChart(data, index, stepsChartLines, stepsLineChart);
        }
    }

    private void addStatsToChart(StatisticData data, int index, Map<String, XYChart.Series> flowChartLines, LineChart<?, ?> flowLineChart) {
        if(index >= 0){
            XYChart.Series series = flowChartLines.get(data.getName());
            XYChart.Data lastFlowAdded = (XYChart.Data) series.getData().get(series.getData().size()-1);
            int timeUsed = Integer.parseInt(lastFlowAdded.getXValue().toString().toString())+data.getTimeUsed();
            series.getData().add(new XYChart.Data<>(Integer.toString(timeUsed), data.getAverageRunTime()));

        }
        else{
            XYChart.Series series = new XYChart.Series<>();
            series.setName(data.getName());
            series.getData().add(new XYChart.Data<>(data.getTimeUsed().toString(), data.getAverageRunTime()));
            flowLineChart.getData().add(series);
            flowChartLines.put(data.getName(), series);
        }
    }
}