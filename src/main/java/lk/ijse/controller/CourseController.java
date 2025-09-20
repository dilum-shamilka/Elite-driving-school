package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.bo.custom.BOFactory;
import lk.ijse.bo.custom.CourseBO;
import lk.ijse.bo.custom.InstructorBO;
import lk.ijse.dto.CourseDTO;
import lk.ijse.dto.InstructorDTO;
import lk.ijse.dto.tm.CourseTM;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CourseController implements Initializable {
    @FXML private TextField txtCourseName;
    @FXML private TextField txtDuration;
    @FXML private TextField txtFee;
    @FXML private ComboBox<String> cmbInstructor;
    @FXML private Button btnSaveCourse;
    @FXML private Button btnUpdateCourse;
    @FXML private Button btnDeleteCourse;
    @FXML private Button btnResetCourse;
    @FXML private TableView<CourseTM> tblCourses;
    @FXML private TableColumn<CourseTM, Integer> colCourseId;
    @FXML private TableColumn<CourseTM, String> colCourseName;
    @FXML private TableColumn<CourseTM, Integer> colDuration;
    @FXML private TableColumn<CourseTM, Double> colFee;
    @FXML private TableColumn<CourseTM, Integer> colInstructorId;
    @FXML private TableColumn<CourseTM, String> colInstructorName;

    private final CourseBO courseBO = (CourseBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.COURSE);
    private final InstructorBO instructorBO = (InstructorBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.INSTRUCTOR);

    private final ObservableList<CourseTM> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tblCourses.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setCellValueFactory();
        loadAllData();
        wireSelectionListener();
        setModeCreate();
    }

    private void setCellValueFactory() {
        colCourseId.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        colCourseName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));
        colInstructorId.setCellValueFactory(new PropertyValueFactory<>("instructorId"));
        colInstructorName.setCellValueFactory(new PropertyValueFactory<>("instructorName"));
    }

    private void loadAllData() {
        try {
            loadAllCourses();
            loadInstructors();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error loading data: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    private void loadAllCourses() throws Exception {
        obList.clear();
        List<CourseDTO> courseList = courseBO.getAllCourses();
        for (CourseDTO course : courseList) {
            obList.add(new CourseTM(
                    course.getCourseId(),
                    course.getName(),
                    course.getDuration(),
                    course.getFee(),
                    course.getInstructorId(),
                    course.getInstructorName()
            ));
        }
        tblCourses.setItems(obList);
    }

    private void loadInstructors() throws Exception {
        cmbInstructor.getItems().clear();
        List<InstructorDTO> instructorList = instructorBO.getAllInstructors();
        for (InstructorDTO instructor : instructorList) {
            cmbInstructor.getItems().add(instructor.getName() + " (" + instructor.getInstructorId() + ")");
        }
    }

    private void wireSelectionListener() {
        tblCourses.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                fillFields(newVal);
                setModeEdit();
            } else {
                clearFields();
                setModeCreate();
            }
        });
    }

    private void fillFields(CourseTM tm) {
        txtCourseName.setText(tm.getName());
        txtDuration.setText(String.valueOf(tm.getDuration()));
        txtFee.setText(String.valueOf(tm.getFee()));
        cmbInstructor.setValue(tm.getInstructorName() + " (" + tm.getInstructorId() + ")");
    }

    private void clearFields() {
        txtCourseName.clear();
        txtDuration.clear();
        txtFee.clear();
        cmbInstructor.getSelectionModel().clearSelection();
        tblCourses.getSelectionModel().clearSelection();
    }

    private void setModeCreate() {
        btnSaveCourse.setDisable(false);
        btnUpdateCourse.setDisable(true);
        btnDeleteCourse.setDisable(true);
    }

    private void setModeEdit() {
        btnSaveCourse.setDisable(true);
        btnUpdateCourse.setDisable(false);
        btnDeleteCourse.setDisable(false);
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!validateFields()) return;

        String name = txtCourseName.getText().trim();
        int duration = Integer.parseInt(txtDuration.getText().trim());
        double fee = Double.parseDouble(txtFee.getText().trim());
        int instructorId = getInstructorIdFromComboBox();
        String instructorName = getInstructorNameFromComboBox();

        try {
            boolean isSaved = courseBO.saveCourse(new CourseDTO(0, name, duration, fee, instructorId, instructorName));
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Course saved successfully.").show();
                loadAllData();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save course.").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        CourseTM selectedCourse = tblCourses.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a course to update.").show();
            return;
        }
        if (!validateFields()) return;

        int courseId = selectedCourse.getCourseId();
        String name = txtCourseName.getText().trim();
        int duration = Integer.parseInt(txtDuration.getText().trim());
        double fee = Double.parseDouble(txtFee.getText().trim());
        int instructorId = getInstructorIdFromComboBox();
        String instructorName = getInstructorNameFromComboBox();

        try {
            boolean isUpdated = courseBO.updateCourse(new CourseDTO(courseId, name, duration, fee, instructorId, instructorName));
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Course updated successfully.").show();
                loadAllData();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update course.").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        CourseTM selectedCourse = tblCourses.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a course to delete.").show();
            return;
        }

        Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this course?", ButtonType.YES, ButtonType.NO).showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean isDeleted = courseBO.deleteCourse(selectedCourse.getCourseId());
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Course deleted successfully.").show();
                    loadAllData();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete course.").show();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
                e.printStackTrace();
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        clearFields();
        setModeCreate();
    }

    private boolean validateFields() {
        if (txtCourseName.getText().trim().isEmpty() || txtDuration.getText().trim().isEmpty() || txtFee.getText().trim().isEmpty() || cmbInstructor.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "All fields are required.").show();
            return false;
        }
        try {
            Integer.parseInt(txtDuration.getText().trim());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Duration must be a valid number.").show();
            return false;
        }
        try {
            Double.parseDouble(txtFee.getText().trim());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Fee must be a valid number.").show();
            return false;
        }
        return true;
    }

    private int getInstructorIdFromComboBox() {
        String selected = cmbInstructor.getValue();
        if (selected != null && !selected.isEmpty()) {
            try {
                return Integer.parseInt(selected.substring(selected.lastIndexOf("(") + 1, selected.lastIndexOf(")")));
            } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                return 0; // Return a default or handle the error
            }
        }
        return 0;
    }

    private String getInstructorNameFromComboBox() {
        String selected = cmbInstructor.getValue();
        if (selected != null && !selected.isEmpty()) {
            return selected.substring(0, selected.lastIndexOf("(") - 1);
        }
        return null;
    }
}