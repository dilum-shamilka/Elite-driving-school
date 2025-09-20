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

public class ReceptionController {
    public TextField txtNewUsername;
    public PasswordField txtNewPassword;
    public PasswordField txtConfirmPassword;

    public void btnSignupOnAction(ActionEvent event) {
        String newUsername = txtNewUsername.getText();
        String newPassword = txtNewPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        if (newPassword.equals(confirmPassword)) {
            // In a real application, you would save the new user and their hashed password to the database.
            // For now, we'll just show a success alert.
            String hashedPassword = PasswordManager.hashPassword(newPassword);
            System.out.println("New Receptionist: " + newUsername);
            System.out.println("Hashed Password: " + hashedPassword);

            new Alert(Alert.AlertType.INFORMATION, "Receptionist registered successfully!").show();

            // Navigate to the dashboard after successful sign-up
            try {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
                Parent root = loader.load();

                // Get the dashboard controller instance and pass the user's role
                DashboardController dashboardController = loader.getController();
                dashboardController.initialize("receptionist");

                Scene scene = new Scene(root);
                stage.setTitle("Driving School Dashboard");
                stage.setScene(scene);
                stage.setMaximized(true);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to load dashboard!").show();
            }

        } else {
            new Alert(Alert.AlertType.ERROR, "Passwords do not match!").show();
        }
    }
}