package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.bo.custom.BOFactory;
import lk.ijse.bo.custom.RoleBO;
import lk.ijse.dto.RoleDTO;
import lk.ijse.dto.tm.RoleTM;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class RoleController implements Initializable {

    @FXML private TextField txtRoleId;
    @FXML private ComboBox<String> cmbRoleName;

    @FXML private Button btnSaveRole;
    @FXML private Button btnUpdateRole;
    @FXML private Button btnDeleteRole;
    @FXML private Button btnResetRole;

    @FXML private TableView<RoleTM> tblRoles;
    @FXML private TableColumn<RoleTM, Integer> colRoleId;
    @FXML private TableColumn<RoleTM, String> colRoleName;

    private final RoleBO roleBO = (RoleBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.ROLE);
    private final ObservableList<RoleTM> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // FIX 1: Populate ComboBox with sample roles. You can fetch these from the database if they are static.
        cmbRoleName.getItems().addAll("Admin", "Reception");

        tblRoles.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setCellValueFactory();
        wireSelectionListener();

        try {
            loadAllRoles();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Initialization Error: " + e.getMessage()).show();
        }

        setModeCreate();
    }

    private void setCellValueFactory() {
        colRoleId.setCellValueFactory(new PropertyValueFactory<>("roleId"));
        colRoleName.setCellValueFactory(new PropertyValueFactory<>("roleName"));
    }

    private void wireSelectionListener() {
        tblRoles.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                fillFields(newVal);
                setModeEdit();
            } else {
                clearFields();
                setModeCreate();
            }
        });
    }

    private void loadAllRoles() throws Exception {
        obList.clear();
        List<RoleDTO> allRoles = roleBO.getAllRoles();
        for (RoleDTO role : allRoles) {
            obList.add(new RoleTM(role.getRoleId(), role.getRoleName()));
        }
        tblRoles.setItems(obList);
    }

    private void fillFields(RoleTM tm) {
        txtRoleId.setText(String.valueOf(tm.getRoleId()));
        cmbRoleName.setValue(tm.getRoleName());
    }

    private void clearFields() {
        txtRoleId.clear();
        cmbRoleName.setValue(null);
        tblRoles.getSelectionModel().clearSelection();
    }

    private void setModeCreate() {
        btnSaveRole.setDisable(false);
        btnUpdateRole.setDisable(true);
        btnDeleteRole.setDisable(true);
        txtRoleId.setDisable(true);
        cmbRoleName.setDisable(false);
    }

    private void setModeEdit() {
        btnSaveRole.setDisable(true);
        btnUpdateRole.setDisable(false);
        btnDeleteRole.setDisable(false);
        txtRoleId.setDisable(true);
        cmbRoleName.setDisable(false);
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String roleName = cmbRoleName.getValue();
        if (roleName == null || roleName.isBlank()) {
            new Alert(Alert.AlertType.WARNING, "Please select a Role Name.").show();
            return;
        }

        try {

            if (roleBO.getByRoleName(roleName) != null) {
                new Alert(Alert.AlertType.WARNING, "Role '" + roleName + "' already exists.").show();
                return;
            }

            boolean isSaved = roleBO.saveRole(new RoleDTO(0, roleName));
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Role saved successfully.").show();
                loadAllRoles();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save role.").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        RoleTM selected = tblRoles.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a role to update.").show();
            return;
        }

        String roleName = cmbRoleName.getValue();
        if (roleName == null || roleName.isBlank()) {
            new Alert(Alert.AlertType.WARNING, "Role Name cannot be empty.").show();
            return;
        }

        try {
            boolean isUpdated = roleBO.updateRole(new RoleDTO(selected.getRoleId(), roleName));
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Role updated successfully.").show();
                loadAllRoles();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update role.").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        RoleTM selected = tblRoles.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a role to delete.").show();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this role?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean isDeleted = roleBO.deleteRole(selected.getRoleId());
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Role deleted successfully.").show();
                    loadAllRoles();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete role.").show();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        clearFields();
        setModeCreate();
    }

    @FXML
    void tblRolesOnMouseClicked(MouseEvent event) {

    }
}