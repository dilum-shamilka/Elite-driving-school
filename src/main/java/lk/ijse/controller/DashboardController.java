package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import javafx.scene.control.Button;

public class DashboardController {

    @FXML
    private StackPane contentArea;

    @FXML
    private Button btnCourses;
    @FXML
    private Button btnInstructors;
    @FXML
    private Button btnRoles;
    @FXML
    private Button btnUsers;
    @FXML
    private Button btnStudents;

    public void initialize(String userRole) {
        if ("receptionist".equals(userRole)) {
            // Disable buttons for the receptionist
            btnCourses.setDisable(true);
            btnInstructors.setDisable(true);
            btnRoles.setDisable(true);
            btnUsers.setDisable(true);

        }
    }

    @FXML
    private void handleCoursesButton(ActionEvent event) throws IOException {
        loadFXML("Course.fxml");
    }

    @FXML
    private void handleInstructorsButton(ActionEvent event) throws IOException {
        loadFXML("Instructor.fxml");
    }

    @FXML
    private void handleLessonsButton(ActionEvent event) throws IOException {
        loadFXML("Lesson.fxml");
    }

    @FXML
    private void handlePaymentsButton(ActionEvent event) throws IOException {
        loadFXML("Payment.fxml");
    }

    @FXML
    private void handleRolesButton(ActionEvent event) throws IOException {
        loadFXML("Role.fxml");
    }

    @FXML
    private void handleUsersButton(ActionEvent event) throws IOException {
        loadFXML("User.fxml");
    }

    @FXML
    private void handleStudentsButton(ActionEvent event) throws IOException {
        loadFXML("Student.fxml");
    }

    private void loadFXML(String fxmlFile) throws IOException {
        var resource = getClass().getResource("/view/" + fxmlFile);
        if (resource == null) {
            throw new IOException("FXML file not found: " + fxmlFile);
        }
        FXMLLoader loader = new FXMLLoader(resource);
        Parent root = loader.load();
        contentArea.getChildren().setAll(root);
    }
}