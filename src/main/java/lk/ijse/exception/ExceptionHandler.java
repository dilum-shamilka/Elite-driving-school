package lk.ijse.exception;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class ExceptionHandler {

    // Removed the unnecessary `import lk.ijse.exception.ExceptionHandler;`

    public static void handleException(Exception e) {
        handleAlert(Alert.AlertType.ERROR, "An Error Occurred", "Details:", e.getMessage());
    }

    public static void handleAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static Optional<ButtonType> handleConfirmation(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
    }
}