package Heatmap;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HeatmapApp extends Application {

    Scene scene;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Onward Heatmap");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("App.fxml"));
        Parent root = fxmlLoader.load();
        HeatmapController controller = (HeatmapController) fxmlLoader.getController();
        primaryStage.setScene(new Scene(root));
        controller.setStage(primaryStage);
        controller.setStageEvent();
        primaryStage.show();
    }

}
