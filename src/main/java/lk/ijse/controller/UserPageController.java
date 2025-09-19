package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.bo.custom.BOFactory;
import lk.ijse.bo.custom.RoleBO;
import lk.ijse.bo.custom.UserBO;
import lk.ijse.dto.RoleDTO;
import lk.ijse.dto.UserDTO;
import lk.ijse.dto.tm.UserTM;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserPageController implements Initializable {
    @FXML private TextField txtUsername;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> cmbRole;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private Button btnSaveUser;
    @FXML private Button btnUpdateUser;
    @FXML private Button btnDeleteUser;
    @FXML private Button btnResetUser;
    @FXML private TableView<UserTM> tblUsers;
    @FXML private TableColumn<UserTM, Integer> colId;
    @FXML private TableColumn<UserTM, String> colUsername;
    @FXML private TableColumn<UserTM, String> colEmail;
    @FXML private TableColumn<UserTM, String> colRole;
    @FXML private TableColumn<UserTM, String> colStatus;

    private final UserBO userBO = (UserBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.USER);
    private final RoleBO roleBO = (RoleBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.ROLE);
    private final ObservableList<UserTM> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbRole.getItems().addAll("Admin", "Reception");
        cmbStatus.getItems().addAll("Active", "Inactive");
        setCellValueFactory();
        loadAllUsers();
        clearFields();
        tblUsers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillFields(newValue);
            }
        });
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadAllUsers() {
        try {
            obList.clear();
            List<UserDTO> allUsers = userBO.getAllUsers();
            for (UserDTO user : allUsers) {
                String roleName = (user.getRole() != null) ? user.getRole().getRoleName() : "N/A";
                obList.add(new UserTM(user.getUserId(), user.getUsername(), user.getEmail(), user.getPassword(), roleName, user.getStatus()));
            }
            tblUsers.setItems(obList);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error loading users: " + e.getMessage()).show();
        }
    }

    private void fillFields(UserTM tm) {
        txtUsername.setText(tm.getUsername());
        txtEmail.setText(tm.getEmail());
        txtPassword.clear();
        cmbRole.setValue(tm.getRole());
        cmbStatus.setValue(tm.getStatus());
        btnSaveUser.setDisable(true);
        btnUpdateUser.setDisable(false);
        btnDeleteUser.setDisable(false);
    }

    private boolean validateFields() {
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText();
        String roleName = cmbRole.getValue();
        String status = cmbStatus.getValue();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || roleName == null || status == null) {
            new Alert(Alert.AlertType.WARNING, "All fields are required.").show();
            return false;
        }

        // Updated Regex for Email Validation
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            new Alert(Alert.AlertType.WARNING, "Please enter a valid email address.").show();
            return false;
        }

        return true;
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText();
        String roleName = cmbRole.getValue();
        String status = cmbStatus.getValue();

        try {
            RoleDTO role = roleBO.getByRoleName(roleName);
            if (role == null) {
                new Alert(Alert.AlertType.ERROR, "Selected role not found. Please ensure 'Admin' and 'Reception' roles exist in the database.").show();
                return;
            }

            UserDTO userDTO = new UserDTO(0, username, email, password, role, status);
            boolean isSaved = userBO.saveUser(userDTO);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "User saved successfully.").show();
                loadAllUsers();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save user.").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        UserTM selectedUser = tblUsers.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a user to update.").show();
            return;
        }

        if (!validateFields()) {
            return;
        }

        int id = selectedUser.getUserId();
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText();
        String roleName = cmbRole.getValue();
        String status = cmbStatus.getValue();

        try {
            RoleDTO role = roleBO.getByRoleName(roleName);
            if (role == null) {
                new Alert(Alert.AlertType.ERROR, "Selected role not found. Please ensure 'Admin' and 'Reception' roles exist in the database.").show();
                return;
            }

            UserDTO userDTO = new UserDTO(id, username, email, password, role, status);
            boolean isUpdated = userBO.updateUser(userDTO);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "User updated successfully.").show();
                loadAllUsers();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update user.").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        UserTM selectedUser = tblUsers.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a user to delete.").show();
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this user?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean isDeleted = userBO.deleteUser(selectedUser.getUserId());
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "User deleted successfully.").show();
                    loadAllUsers();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete user.").show();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtUsername.clear();
        txtEmail.clear();
        txtPassword.clear();
        cmbRole.setValue(null);
        cmbStatus.setValue(null);
        tblUsers.getSelectionModel().clearSelection();
        btnSaveUser.setDisable(false);
        btnUpdateUser.setDisable(true);
        btnDeleteUser.setDisable(true);
    }
}