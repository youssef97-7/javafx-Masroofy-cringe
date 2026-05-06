package com.masroofy.controller;

import com.masroofy.model.databaseManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class PinEntryController {

    @FXML private Label titleLabel;
    @FXML private PasswordField pinField;
    @FXML private Button actionButton;

    private int attempts = 0;
    private int storedPin = -1;
    private Runnable onSuccess;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setOnSuccess(Runnable onSuccess) {
        this.onSuccess = onSuccess;
    }

    @FXML
    public void initialize() {
        storedPin = databaseManager.getPin();
        titleLabel.setText("Enter your Security PIN");
        actionButton.setText("Unlock");
    }

    @FXML
    private void handleAction() {
        String input = pinField.getText();
        if (input.isEmpty() || !input.matches("\\d+")) {
            showAlert("Invalid Input", "Please enter a numeric PIN.");
            return;
        }

        int enteredPin = Integer.parseInt(input);

        if (enteredPin == storedPin) {
            if (onSuccess != null) onSuccess.run();
        } else {
            attempts++;
            if (attempts >= 3) {
                showAlert("Access Denied", "Too many failed attempts. Application will close.");
                System.exit(0);
            } else {
                showAlert("Incorrect PIN", "Invalid PIN. " + (3 - attempts) + " attempts remaining.");
                pinField.clear();
            }
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
