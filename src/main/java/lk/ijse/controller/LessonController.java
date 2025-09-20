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
import lk.ijse.bo.custom.LessonBO;
import lk.ijse.bo.custom.StudentBO;
import lk.ijse.dto.CourseDTO;
import lk.ijse.dto.InstructorDTO;
import lk.ijse.dto.LessonDTO;
import lk.ijse.dto.StudentDTO;
import lk.ijse.dto.tm.LessonTM;

import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class LessonController implements Initializable {
    @FXML private DatePicker dpDate;
    @FXML private TextField txtTime;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private ComboBox<String> cmbInstructor;
    @FXML private ComboBox<String> cmbCourse;
    @FXML private ComboBox<String> cmbStudent;

    @FXML private Button btnSaveLesson;
    @FXML private Button btnUpdateLesson;
    @FXML private Button btnDeleteLesson;
    @FXML private Button btnResetLesson;

    @FXML private TableView<LessonTM> tblLessons;
    @FXML private TableColumn<LessonTM, Integer> colLessonId;
    @FXML private TableColumn<LessonTM, Date> colDate;
    @FXML private TableColumn<LessonTM, String> colTime;
    @FXML private TableColumn<LessonTM, String> colStatus;
    @FXML private TableColumn<LessonTM, Integer> colInstructorId;
    @FXML private TableColumn<LessonTM, Integer> colCourseId;
    @FXML private TableColumn<LessonTM, Integer> colStudentId;

    private final LessonBO lessonBO = (LessonBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.LESSON);
    private final InstructorBO instructorBO = (InstructorBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.INSTRUCTOR);
    private final CourseBO courseBO = (CourseBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.COURSE);
    private final StudentBO studentBO = (StudentBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.STUDENT);

    // No need for a global ObservableList since we'll create a new one each time.
    // private final ObservableList<LessonTM> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tblLessons.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setCellValueFactory();
        wireSelectionListener();
        populateStatusComboBox();

        try {
            loadAllLessons();
            loadComboBoxes();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Initialization Error: Failed to load data. " + e.getMessage()).show();
            e.printStackTrace();
        }

        setModeCreate();
    }

    private void setCellValueFactory() {
        colLessonId.setCellValueFactory(new PropertyValueFactory<>("lessonId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colInstructorId.setCellValueFactory(new PropertyValueFactory<>("instructorId"));
        colCourseId.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
    }

    private void wireSelectionListener() {
        tblLessons.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                fillFields(newVal);
                setModeEdit();
            } else {
                clearFields();
                setModeCreate();
            }
        });
    }

    private void populateStatusComboBox() {
        cmbStatus.getItems().addAll("scheduled", "completed", "canceled");
    }

    private void loadComboBoxes() throws Exception {
        // Load Instructors
        cmbInstructor.getItems().clear();
        List<InstructorDTO> instructors = instructorBO.getAllInstructors();
        for (InstructorDTO i : instructors) {
            cmbInstructor.getItems().add(i.getName() + " (" + i.getInstructorId() + ")");
        }

        // Load Courses
        cmbCourse.getItems().clear();
        List<CourseDTO> courses = courseBO.getAllCourses();
        for (CourseDTO c : courses) {
            cmbCourse.getItems().add(c.getName() + " (" + c.getCourseId() + ")");
        }

        // Load Students
        cmbStudent.getItems().clear();
        List<StudentDTO> students = studentBO.getAllStudents();
        for (StudentDTO s : students) {
            cmbStudent.getItems().add(s.getName() + " (" + s.getStudentId() + ")");
        }
    }

    /**
     * Fix: Creates a new ObservableList each time to ensure the table refreshes.
     */
    private void loadAllLessons() throws Exception {
        ObservableList<LessonTM> freshList = FXCollections.observableArrayList();
        List<LessonDTO> allLessons = lessonBO.getAllLessons();
        for (LessonDTO lesson : allLessons) {
            freshList.add(new LessonTM(
                    lesson.getLessonId(),
                    lesson.getDate(),
                    lesson.getTime(),
                    lesson.getStatus(),
                    lesson.getInstructorId(),
                    lesson.getCourseId(),
                    lesson.getStudentId()
            ));
        }
        tblLessons.setItems(freshList);
        tblLessons.getSelectionModel().clearSelection();
    }

    private void fillFields(LessonTM tm) {
        dpDate.setValue(tm.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        txtTime.setText(tm.getTime());
        cmbStatus.setValue(tm.getStatus());
        cmbInstructor.setValue(getInstructorNameFromId(tm.getInstructorId()));
        cmbCourse.setValue(getCourseNameFromId(tm.getCourseId()));
        cmbStudent.setValue(getStudentNameFromId(tm.getStudentId()));
    }

    private String getInstructorNameFromId(int id) {
        try {
            InstructorDTO instructor = instructorBO.getInstructor(id);
            return instructor != null ? instructor.getName() + " (" + instructor.getInstructorId() + ")" : null;
        } catch (Exception e) {
            return null;
        }
    }

    private String getCourseNameFromId(int id) {
        try {
            CourseDTO course = courseBO.getCourse(id);
            return course != null ? course.getName() + " (" + course.getCourseId() + ")" : null;
        } catch (Exception e) {
            return null;
        }
    }

    private String getStudentNameFromId(int id) {
        try {
            StudentDTO student = studentBO.getStudent(id);
            return student != null ? student.getName() + " (" + student.getStudentId() + ")" : null;
        } catch (Exception e) {
            return null;
        }
    }

    private void clearFields() {
        dpDate.setValue(null);
        txtTime.clear();
        cmbStatus.getSelectionModel().clearSelection();
        cmbInstructor.getSelectionModel().clearSelection();
        cmbCourse.getSelectionModel().clearSelection();
        cmbStudent.getSelectionModel().clearSelection();
    }

    private void setModeCreate() {
        btnSaveLesson.setDisable(false);
        btnUpdateLesson.setDisable(true);
        btnDeleteLesson.setDisable(true);
    }

    private void setModeEdit() {
        btnSaveLesson.setDisable(true);
        btnUpdateLesson.setDisable(false);
        btnDeleteLesson.setDisable(false);
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        Date date = dpDate.getValue() != null ? Date.from(dpDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
        String time = txtTime.getText();
        String status = cmbStatus.getValue();
        int instructorId = Integer.parseInt(cmbInstructor.getValue().split("\\(")[1].replace(")", ""));
        int courseId = Integer.parseInt(cmbCourse.getValue().split("\\(")[1].replace(")", ""));
        int studentId = Integer.parseInt(cmbStudent.getValue().split("\\(")[1].replace(")", ""));

        if (date == null || time.isBlank() || status == null) {
            new Alert(Alert.AlertType.WARNING, "All fields are required.").show();
            return;
        }

        try {
            LessonDTO dto = new LessonDTO(0, date, time, status, instructorId, courseId, studentId);
            boolean ok = lessonBO.saveLesson(dto);
            if (ok) {
                new Alert(Alert.AlertType.INFORMATION, "Lesson saved successfully.").show();
                loadAllLessons(); // Call the fixed method
                clearFields();
                setModeCreate();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save lesson.").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        LessonTM selected = tblLessons.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a lesson to update.").show();
            return;
        }

        Date date = dpDate.getValue() != null ? Date.from(dpDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
        String time = txtTime.getText();
        String status = cmbStatus.getValue();
        int instructorId = Integer.parseInt(cmbInstructor.getValue().split("\\(")[1].replace(")", ""));
        int courseId = Integer.parseInt(cmbCourse.getValue().split("\\(")[1].replace(")", ""));
        int studentId = Integer.parseInt(cmbStudent.getValue().split("\\(")[1].replace(")", ""));

        if (date == null || time.isBlank() || status == null) {
            new Alert(Alert.AlertType.WARNING, "All fields are required.").show();
            return;
        }

        try {
            LessonDTO dto = new LessonDTO(selected.getLessonId(), date, time, status, instructorId, courseId, studentId);
            boolean ok = lessonBO.updateLesson(dto);
            if (ok) {
                new Alert(Alert.AlertType.INFORMATION, "Lesson updated successfully.").show();
                loadAllLessons(); // Call the fixed method
                clearFields();
                setModeCreate();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update lesson.").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        LessonTM selected = tblLessons.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a lesson to delete.").show();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this lesson?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> res = confirm.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.YES) {
            try {
                boolean ok = lessonBO.deleteLesson(selected.getLessonId());
                if (ok) {
                    new Alert(Alert.AlertType.INFORMATION, "Lesson deleted successfully.").show();
                    loadAllLessons(); // Call the fixed method
                    clearFields();
                    setModeCreate();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete lesson.").show();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        clearFields();
        tblLessons.getSelectionModel().clearSelection();
        setModeCreate();
    }
}