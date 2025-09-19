package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.bo.custom.BOFactory;
import lk.ijse.bo.custom.StudentBO;
import lk.ijse.dto.StudentDTO;
import lk.ijse.dto.tm.StudentTM;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class StudentController implements Initializable {
    @FXML private TextField txtName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private TextField txtAddress;
    @FXML private DatePicker dpDob;

    @FXML private Button btnSaveStudent;
    @FXML private Button btnUpdateStudent;
    @FXML private Button btnDeleteStudent;
    @FXML private Button btnResetStudent;

    @FXML private TableView<StudentTM> tblStudents;
    @FXML private TableColumn<StudentTM, Integer> colStudentId;
    @FXML private TableColumn<StudentTM, String> colName;
    @FXML private TableColumn<StudentTM, String> colEmail;
    @FXML private TableColumn<StudentTM, String> colPhone;
    @FXML private TableColumn<StudentTM, String> colAddress;
    @FXML private TableColumn<StudentTM, Date> colDob;

    @FXML private Button btnLoadAllStudents;
    @FXML private Button btnFindAllStudents;
    @FXML private Button btnStudentsWithCourses;

    private final StudentBO studentBO = (StudentBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.STUDENT);
    private final ObservableList<StudentTM> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tblStudents.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setCellValueFactory();
        wireSelectionListener();

        try {
            loadAllStudents();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Initialization Error: " + e.getMessage()).show();
        }

        setModeCreate();
    }

    private void setCellValueFactory() {
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
    }

    private void wireSelectionListener() {
        tblStudents.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                fillFields(newVal);
                setModeEdit();
            } else {
                clearFields();
                setModeCreate();
            }
        });
    }

    private void loadAllStudents() throws Exception {
        obList.clear();
        List<StudentDTO> allStudents = studentBO.getAllStudents();
        for (StudentDTO student : allStudents) {
            obList.add(new StudentTM(
                    student.getStudentId(),
                    student.getName(),
                    student.getEmail(),
                    student.getPhone(),
                    student.getAddress(),
                    student.getDob()
            ));
        }
        tblStudents.setItems(obList);
        tblStudents.getSelectionModel().clearSelection();
    }

    private void fillFields(StudentTM tm) {
        txtName.setText(tm.getName());
        txtEmail.setText(tm.getEmail());
        txtPhone.setText(tm.getPhone());
        txtAddress.setText(tm.getAddress());
        if (tm.getDob() != null) {
            dpDob.setValue(tm.getDob().toLocalDate());
        } else {
            dpDob.setValue(null);
        }
    }

    private void clearFields() {
        txtName.clear();
        txtEmail.clear();
        txtPhone.clear();
        txtAddress.clear();
        dpDob.setValue(null);
    }

    private void setModeCreate() {
        btnSaveStudent.setDisable(false);
        btnUpdateStudent.setDisable(true);
        btnDeleteStudent.setDisable(true);
    }

    private void setModeEdit() {
        btnSaveStudent.setDisable(true);
        btnUpdateStudent.setDisable(false);
        btnDeleteStudent.setDisable(false);
    }

    private boolean validateFields() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String address = txtAddress.getText().trim();
        LocalDate dob = dpDob.getValue();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || dob == null) {
            new Alert(Alert.AlertType.WARNING, "All fields are required.").show();
            return false;
        }

        // Updated Regex for Email Validation
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            new Alert(Alert.AlertType.WARNING, "Please enter a valid email address.").show();
            return false;
        }

        // Updated Regex for Sri Lankan Phone Number Validation
        if (!phone.matches("^0\\d{9}$")) {
            new Alert(Alert.AlertType.WARNING, "Please enter a valid 10-digit Sri Lankan phone number (e.g., 0712345678).").show();
            return false;
        }

        return true;
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String address = txtAddress.getText().trim();
        Date dob = Date.valueOf(dpDob.getValue());

        try {
            boolean ok = studentBO.saveStudent(new StudentDTO(0, name, email, phone, address, dob));
            if (ok) {
                new Alert(Alert.AlertType.INFORMATION, "Student saved successfully.").show();
                loadAllStudents();
                clearFields();
                setModeCreate();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save student.").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        StudentTM selected = tblStudents.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a student to update.").show();
            return;
        }

        if (!validateFields()) {
            return;
        }

        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String address = txtAddress.getText().trim();
        Date dob = Date.valueOf(dpDob.getValue());

        try {
            boolean ok = studentBO.updateStudent(new StudentDTO(
                    selected.getStudentId(), name, email, phone, address, dob
            ));
            if (ok) {
                new Alert(Alert.AlertType.INFORMATION, "Student updated successfully.").show();
                loadAllStudents();
                clearFields();
                setModeCreate();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update student.").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        StudentTM selected = tblStudents.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a student to delete.").show();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this student?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> res = confirm.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.YES) {
            try {
                boolean ok = studentBO.deleteStudent(selected.getStudentId());
                if (ok) {
                    new Alert(Alert.AlertType.INFORMATION, "Student deleted successfully.").show();
                    loadAllStudents();
                    clearFields();
                    setModeCreate();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete student.").show();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        tblStudents.getSelectionModel().clearSelection();
        clearFields();
        setModeCreate();
    }

    @FXML
    void btnLoadAllStudentsOnAction(ActionEvent event) {
        try {
            loadAllStudents();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "An error occurred while loading all students: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnFindAllStudentsOnAction(ActionEvent event) {
        try {
            List<StudentDTO> students = studentBO.getStudentsRegisteredInAllCourses();

            obList.clear();

            if (students.isEmpty()) {
                new Alert(Alert.AlertType.INFORMATION, "No students found who are registered for all courses.").show();
            } else {
                for (StudentDTO student : students) {
                    obList.add(new StudentTM(
                            student.getStudentId(),
                            student.getName(),
                            student.getEmail(),
                            student.getPhone(),
                            student.getAddress(),
                            student.getDob()
                    ));
                }
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "An error occurred while fetching the list: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnStudentsWithCoursesOnAction(ActionEvent event) {
        try {
            Map<StudentDTO, List<String>> studentCoursesMap = studentBO.getStudentsWithCourses();

            StringBuilder resultText = new StringBuilder();
            if (studentCoursesMap.isEmpty()) {
                resultText.append("No students found with enrolled courses.");
            } else {
                resultText.append("Students and Their Enrolled Courses:\n\n");
                for (Map.Entry<StudentDTO, List<String>> entry : studentCoursesMap.entrySet()) {
                    StudentDTO student = entry.getKey();
                    List<String> courses = entry.getValue();

                    resultText.append("Student ID: ").append(student.getStudentId()).append(", Name: ").append(student.getName()).append("\n");
                    resultText.append("   Enrolled Courses: ").append(String.join(", ", courses)).append("\n\n");
                }
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Student Course Details");
            alert.setHeaderText(null);
            alert.setContentText(resultText.toString());
            alert.getDialogPane().setPrefSize(600, 400);
            alert.showAndWait();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "An error occurred while fetching the list: " + e.getMessage()).show();
        }
    }
}