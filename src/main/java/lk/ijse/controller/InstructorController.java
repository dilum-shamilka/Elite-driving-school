package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lk.ijse.bo.custom.BOFactory;
import lk.ijse.bo.custom.InstructorBO;
import lk.ijse.dto.InstructorDTO;
import lk.ijse.dto.tm.InstructorTM;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class InstructorController implements Initializable {
    @FXML private TextField txtName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private ComboBox<String> cmbAvailability;
    @FXML private ComboBox<String> cmbSpecialization;
    @FXML private Button btnSaveInstructor;
    @FXML private Button btnUpdateInstructor;
    @FXML private Button btnDeleteInstructor;
    @FXML private Button btnResetInstructor;
    @FXML private TableView<InstructorTM> tblInstructors;
    @FXML private TableColumn<InstructorTM, Integer> colInstructorId;
    @FXML private TableColumn<InstructorTM, String> colName;
    @FXML private TableColumn<InstructorTM, String> colEmail;
    @FXML private TableColumn<InstructorTM, String> colPhone;
    @FXML private TableColumn<InstructorTM, String> colAvailability;
    @FXML private TableColumn<InstructorTM, String> colSpecialization;

    private final InstructorBO instructorBO = (InstructorBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.INSTRUCTOR);
    private final ObservableList<InstructorTM> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tblInstructors.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setCellValueFactory();
        wireSelectionListener();
        wireEnterKeyToSave();

        cmbAvailability.getItems().addAll("Full-Time", "Part-Time", "On-Call");
        // Updated specialization options for a driving school
        cmbSpecialization.getItems().addAll("Basic Learner Program","Advanced Defensive Driving", "Motorcycle License Training", "Heavy Vehicle Training", "Refresher Driving Course");

        try {
            loadAllInstructors();
        } catch (Exception e) {
            show(Alert.AlertType.ERROR, "Initialization Error: " + e.getMessage());
        }
        setModeCreate();
    }

    private void setCellValueFactory() {
        colInstructorId.setCellValueFactory(new PropertyValueFactory<>("instructorId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colAvailability.setCellValueFactory(new PropertyValueFactory<>("availability"));
        colSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
    }

    private void wireSelectionListener() {
        tblInstructors.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                fillFields(newVal);
                setModeEdit();
            } else {
                clearFields();
                setModeCreate();
            }
        });
    }

    private void wireEnterKeyToSave() {
        cmbSpecialization.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ENTER) {
                btnSaveOnAction(new ActionEvent());
                e.consume();
            }
        });
    }

    private void loadAllInstructors() throws Exception {
        obList.clear();
        List<InstructorDTO> allInstructors = instructorBO.getAllInstructors();
        for (InstructorDTO instructor : allInstructors) {
            obList.add(new InstructorTM(instructor.getInstructorId(), instructor.getName(), instructor.getEmail(), instructor.getPhone(), instructor.getAvailability(), instructor.getSpecialization()));
        }
        tblInstructors.setItems(obList);
        tblInstructors.getSelectionModel().clearSelection();
    }

    private void fillFields(InstructorTM tm) {
        txtName.setText(tm.getName());
        txtEmail.setText(tm.getEmail());
        txtPhone.setText(tm.getPhone());
        cmbAvailability.setValue(tm.getAvailability());
        cmbSpecialization.setValue(tm.getSpecialization());
    }

    private void clearFields() {
        txtName.clear();
        txtEmail.clear();
        txtPhone.clear();
        cmbAvailability.getSelectionModel().clearSelection();
        cmbSpecialization.getSelectionModel().clearSelection();
    }

    private void setModeCreate() {
        btnSaveInstructor.setDisable(false);
        btnUpdateInstructor.setDisable(true);
        btnDeleteInstructor.setDisable(true);
    }

    private void setModeEdit() {
        btnSaveInstructor.setDisable(true);
        btnUpdateInstructor.setDisable(false);
        btnDeleteInstructor.setDisable(false);
    }

    private void show(Alert.AlertType type, String msg) {
        new Alert(type, msg).show();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String name = txtName.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String availability = cmbAvailability.getValue();
        String specialization = cmbSpecialization.getValue();

        if (!validateFields()) {
            return;
        }

        try {
            boolean ok = instructorBO.saveInstructor(new InstructorDTO(0, name.trim(), email.trim(), phone.trim(), availability, specialization));
            if (ok) {
                show(Alert.AlertType.INFORMATION, "Instructor saved successfully.");
                loadAllInstructors();
                clearFields();
                setModeCreate();
            } else {
                show(Alert.AlertType.ERROR, "Failed to save instructor.");
            }
        } catch (Exception e) {
            show(Alert.AlertType.ERROR, "Database error: " + e.getMessage());
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        InstructorTM selected = tblInstructors.getSelectionModel().getSelectedItem();
        if (selected == null) {
            show(Alert.AlertType.WARNING, "Please select an instructor to update.");
            return;
        }

        String name = txtName.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String availability = cmbAvailability.getValue();
        String specialization = cmbSpecialization.getValue();

        if (!validateFields()) {
            return;
        }

        try {
            boolean ok = instructorBO.updateInstructor(new InstructorDTO(selected.getInstructorId(), name.trim(), email.trim(), phone.trim(), availability, specialization));
            if (ok) {
                show(Alert.AlertType.INFORMATION, "Instructor updated successfully.");
                loadAllInstructors();
                clearFields();
                setModeCreate();
            } else {
                show(Alert.AlertType.ERROR, "Failed to update instructor.");
            }
        } catch (Exception e) {
            show(Alert.AlertType.ERROR, "Database error: " + e.getMessage());
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        InstructorTM selected = tblInstructors.getSelectionModel().getSelectedItem();
        if (selected == null) {
            show(Alert.AlertType.WARNING, "Please select an instructor to delete.");
            return;
        }

        Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this instructor?", ButtonType.YES, ButtonType.NO).showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean ok = instructorBO.deleteInstructor(selected.getInstructorId());
                if (ok) {
                    show(Alert.AlertType.INFORMATION, "Instructor deleted successfully.");
                    loadAllInstructors();
                    clearFields();
                    setModeCreate();
                } else {
                    show(Alert.AlertType.ERROR, "Failed to delete instructor.");
                }
            } catch (Exception e) {
                show(Alert.AlertType.ERROR, "Database error: " + e.getMessage());
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        clearFields();
        setModeCreate();
    }

    private boolean validateFields() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String availability = cmbAvailability.getValue();
        String specialization = cmbSpecialization.getValue();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || availability == null || specialization == null) {
            show(Alert.AlertType.WARNING, "All fields are required.");
            return false;
        }

        // Regex for email validation
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            show(Alert.AlertType.WARNING, "Please enter a valid email address.");
            return false;
        }

        // Regex for Sri Lankan phone number validation (e.g., 0712345678)
        if (!phone.matches("^0\\d{9}$")) {
            show(Alert.AlertType.WARNING, "Please enter a valid 10-digit Sri Lankan phone number (e.g., 0712345678).");
            return false;
        }
        return true;
    }
}