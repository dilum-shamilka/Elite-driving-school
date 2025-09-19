package lk.ijse;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class Initializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load the new main_login.fxml file
        URL mainLoginURL = getClass().getResource("/view/mainlogin.fxml");
        if (mainLoginURL == null) {
            System.err.println("Error: main_login.fxml not found. Please ensure it's in src/main/resources/view/");
            return;
        }

        Parent root = FXMLLoader.load(Objects.requireNonNull(mainLoginURL));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Driving School Management System");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}
