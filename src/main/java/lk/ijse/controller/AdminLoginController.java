package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.util.PasswordManager;

import java.io.IOException;
import java.util.Objects;

public class AdminLoginController {

    public TextField txtUsername;
    public PasswordField txtPassword;

    public void btnLoginOnAction(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        // This should be fetched from a database for a real application
        String adminUsername = "d";
        String storedHashedPassword = "$2a$10$qhJazpxJDngh.Csso9mAleQPJoX7ikPkq.thPKH3WmlSFv.jcys6C";

        if (username.equals(adminUsername) && PasswordManager.checkPassword(password, storedHashedPassword)) {
            try {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
                Parent root = loader.load();

                // Get the dashboard controller instance and pass the user's role
                DashboardController dashboardController = loader.getController();
                dashboardController.initialize("admin");

                Scene scene = new Scene(root);
                stage.setTitle("Driving school - Admin Dashboard");
                stage.setScene(scene);
                stage.setMaximized(true);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to load dashboard!").show();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Invalid Username or Password!").show();
        }
    }
}