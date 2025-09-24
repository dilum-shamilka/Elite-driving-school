package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.bo.custom.BOFactory;
import lk.ijse.bo.custom.LessonBO;
import lk.ijse.bo.custom.PaymentBO;
import lk.ijse.bo.custom.StudentBO;
import lk.ijse.dto.LessonDTO;
import lk.ijse.dto.PaymentDTO;
import lk.ijse.dto.tm.PaymentTM;

import java.net.URL;
import java.sql.Date; // You can remove this or change to java.util.Date
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {

    @FXML private TextField txtAmount;
    @FXML private DatePicker dpDate;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private ComboBox<String> cmbStudent;
    @FXML private ComboBox<String> cmbLesson;
    @FXML private Button btnSavePayment;
    @FXML private Button btnUpdatePayment;
    @FXML private Button btnDeletePayment;
    @FXML private Button btnResetPayment;
    @FXML private TableView<PaymentTM> tblPayments;
    @FXML private TableColumn<PaymentTM, Integer> colPaymentId;
    @FXML private TableColumn<PaymentTM, Double> colAmount;
    @FXML private TableColumn<PaymentTM, java.util.Date> colDate; // Change here
    @FXML private TableColumn<PaymentTM, String> colStatus;
    @FXML private TableColumn<PaymentTM, Integer> colStudentId;
    @FXML private TableColumn<PaymentTM, Integer> colLessonId;

    private final PaymentBO paymentBO = (PaymentBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.PAYMENT);
    private final StudentBO studentBO = (StudentBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.STUDENT);
    private final LessonBO lessonBO = (LessonBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.LESSON);

    private final ObservableList<PaymentTM> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbStatus.getItems().addAll("Paid", "Advance", "Not Paid");
        setCellValueFactory();
        loadAllPayments();
        loadStudents();
        loadLessons();
        clearFields();

        tblPayments.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) fillFields(newValue);
        });
    }

    private void setCellValueFactory() {
        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colLessonId.setCellValueFactory(new PropertyValueFactory<>("lessonId"));
    }

    private void loadAllPayments() {
        try {
            obList.clear();
            List<PaymentDTO> payments = paymentBO.getAllPayments();
            for (PaymentDTO dto : payments) {
                // Corrected constructor call, no casting needed
                obList.add(new PaymentTM(dto.getPaymentId(), dto.getAmount(), dto.getDate(), dto.getStatus(), dto.getStudentId(), dto.getLessonId()));
            }
            tblPayments.setItems(obList);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error loading payments: " + e.getMessage()).show();
        }
    }

    private void loadStudents() {
        try {
            List<String> students = studentBO.getAllStudentIds();
            cmbStudent.getItems().addAll(students);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error loading students: " + e.getMessage()).show();
        }
    }

    private void loadLessons() {
        try {
            List<LessonDTO> lessons = lessonBO.getAllLessons();
            for (LessonDTO lesson : lessons) {
                cmbLesson.getItems().add(String.valueOf(lesson.getLessonId()));
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error loading lessons: " + e.getMessage()).show();
        }
    }

    private void fillFields(PaymentTM tm) {
        txtAmount.setText(String.valueOf(tm.getAmount()));
        if (tm.getDate() != null) {
            dpDate.setValue(tm.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
            dpDate.setValue(null);
        }
        cmbStatus.setValue(tm.getStatus());
        cmbStudent.setValue(String.valueOf(tm.getStudentId()));
        cmbLesson.setValue(String.valueOf(tm.getLessonId()));
        btnSavePayment.setDisable(true);
        btnUpdatePayment.setDisable(false);
        btnDeletePayment.setDisable(false);
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        try {
            double amount = Double.parseDouble(txtAmount.getText());
            LocalDate localDate = dpDate.getValue();
            String status = cmbStatus.getValue();
            int studentId = Integer.parseInt(cmbStudent.getValue());
            int lessonId = Integer.parseInt(cmbLesson.getValue());


            PaymentDTO dto = new PaymentDTO(0, amount, java.util.Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), status, studentId, lessonId);
            if (paymentBO.savePayment(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Payment saved!").show();
                loadAllPayments();
                clearFields();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error saving payment: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        PaymentTM selected = tblPayments.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Select a payment to update!").show();
            return;
        }
        try {
            double amount = Double.parseDouble(txtAmount.getText());
            LocalDate localDate = dpDate.getValue();
            String status = cmbStatus.getValue();
            int studentId = Integer.parseInt(cmbStudent.getValue());
            int lessonId = Integer.parseInt(cmbLesson.getValue());

            PaymentDTO dto = new PaymentDTO(selected.getPaymentId(), amount, java.util.Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), status, studentId, lessonId);
            if (paymentBO.updatePayment(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Payment updated!").show();
                loadAllPayments();
                clearFields();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error updating payment: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        PaymentTM selected = tblPayments.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Select a payment to delete!").show();
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                if (paymentBO.deletePayment(selected.getPaymentId())) {
                    new Alert(Alert.AlertType.INFORMATION, "Payment deleted!").show();
                    loadAllPayments();
                    clearFields();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Error deleting payment: " + e.getMessage()).show();
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtAmount.clear();
        dpDate.setValue(null);
        cmbStatus.setValue(null);
        cmbStudent.setValue(null);
        cmbLesson.setValue(null);
        tblPayments.getSelectionModel().clearSelection();
        btnSavePayment.setDisable(false);
        btnUpdatePayment.setDisable(true);
        btnDeletePayment.setDisable(true);
    }
}