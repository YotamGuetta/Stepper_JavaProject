
// MainApp.java
import AdministratorUI.main.MainAdminController;
import main.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application{
    private String main = "main/StepperSceneLayout.fxml";
    private String admin = "AdministratorUI/main/StepperAdminSceneLayout.fxml";
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(admin));

        Parent root = loader.load();

        MainAdminController mainController = loader.getController();

        mainController.SetPrimaryStage(primaryStage);

        // Add any additional initialization for the controller

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("main/MainStylesDiablo.css")).toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {launch(args);}

}
