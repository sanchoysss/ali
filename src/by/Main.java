package by;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("views/MainView.fxml")));

        Scene myScene = new Scene(root);

        primaryStage.setScene(myScene);
        primaryStage.setTitle("3D");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
}
