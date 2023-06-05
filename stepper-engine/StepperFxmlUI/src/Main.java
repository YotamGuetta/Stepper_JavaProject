
// MainApp.java
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import main.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        Font dfont = Font.loadFont(getClass().getResourceAsStream("main/diablo-font.ttf"), 12);


        FXMLLoader loader = new FXMLLoader(getClass().getResource("main/StepperSceneLayout.fxml"));

        Parent root = loader.load();
        MainController mainController = loader.getController();
        mainController.SetPrimaryStage(primaryStage);

        // Add any additional initialization for the controller

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("main/MainStyles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

}
