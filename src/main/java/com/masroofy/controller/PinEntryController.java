package com.masroofy.controller;

import com.masroofy.model.databaseManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controller for the security PIN entry screen.
 * Handles user authentication, PIN validation from the database,
 * and enforces a maximum attempt limit before locking the application.
 */
public class PinEntryController {

    @FXML private Label titleLabel;
    @FXML private PasswordField pinField;
    @FXML private Button actionButton;
    /** Tracks the number of consecutive incorrect PIN entries. */
    private int attempts = 0;
    /** The authenticated PIN retrieved from the database. */
    private int storedPin = -1;
    /** Callback executed upon a successful PIN match. */
    private Runnable onSuccess;
    /** The primary stage for this controller's view. */
    private Stage stage;

    /**
     * Sets the stage for this controller.
     *
     * @param stage The Stage object to be managed.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Sets the action to be performed when the user enters the correct PIN.
     *
     * @param onSuccess A Runnable containing the success logic.
     */
    public void setOnSuccess(Runnable onSuccess) {
        this.onSuccess = onSuccess;
    }

    /**
     * Initializes the controller by fetching the stored PIN from the database
     * and setting up the initial UI text.
     */
    @FXML
    public void initialize() {
        storedPin = databaseManager.getPin();
        titleLabel.setText("Enter your Security PIN");
        actionButton.setText("Unlock");
    }

    /**
     * Validates the user's input against the stored PIN.
     * If correct, triggers the success callback; otherwise, increments the
     * failure count and terminates the app after 3 failed attempts.
     */
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

    /**
     * Helper method to display a standardized information alert to the user.
     *
     * @param title   The title of the alert window.
     * @param content The message to be displayed in the alert body.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
