package com.masroofy.controller;

import com.masroofy.model.BudgetCycle;
import com.masroofy.model.databaseManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Controller for the Settings view.
 * Provides functionality for modifying the budget cycle dates, updating the total allowance,
 * changing the security PIN, and performing a factory reset of the application data.
 */
public class SettingsController {

    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField allowanceField;
    @FXML private PasswordField currentPinField;
    @FXML private PasswordField newPinField;
    @FXML private PasswordField confirmPinField;

    /** Logic to execute when returning to the dashboard. */
    private Runnable backToDashboardHandler;
    /** Logic to execute after the budget cycle dates or allowance are modified. */
    private Runnable onCycleUpdatedHandler;
    /** Logic to execute after the database has been completely cleared. */
    private Runnable onClearAllHandler;

    /**
     * Sets the callback for navigating back to the dashboard.
     * @param handler The navigation logic.
     */
    public void setBackToDashboardHandler(Runnable handler) {
        this.backToDashboardHandler = handler;
    }

    /**
     * Sets the callback for after a successful budget cycle update.
     * @param handler The refresh logic.
     */
    public void setOnCycleUpdatedHandler(Runnable handler) {
        this.onCycleUpdatedHandler = handler;
    }

    /**
     * Sets the callback for after a "Clear All" operation.
     * @param handler The cleanup logic.
     */
    public void setOnClearAllHandler(Runnable handler) {
        this.onClearAllHandler = handler;
    }

    /**
     * Populates the settings fields with existing budget cycle data.
     *
     * @param start     The current cycle start date.
     * @param end       The current cycle end date.
     * @param allowance The current total budget amount.
     */
    public void setInitialData(LocalDate start, LocalDate end, double allowance) {
        startDatePicker.setValue(start);
        endDatePicker.setValue(end);
        allowanceField.setText(String.valueOf(allowance));
    }

    /**
     * Validates the user input and updates the budget cycle in the database.
     * Triggers the update handler if the operation is successful.
     */
    @FXML
    private void handleUpdateCycle() {
        try {
            LocalDate start = startDatePicker.getValue();
            LocalDate end = endDatePicker.getValue();
            double allowance = Double.parseDouble(allowanceField.getText());

            if (start == null || end == null || end.isBefore(start)) {
                showAlert(Alert.AlertType.ERROR, "Invalid Dates", "Please ensure start date is before end date.");
                return;
            }

            databaseManager.updateCycle(allowance, start, end);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Budget cycle updated successfully. Expenses outside the range have been removed.");
            
            if (onCycleUpdatedHandler != null) {
                onCycleUpdatedHandler.run();
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number for the allowance.");
        }
    }

    /**
     * Prompts the user for confirmation before wiping all application data
     * from the database and resetting the UI state.
     */
    @FXML
    private void handleClearAll() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Clear All");
        alert.setHeaderText("Are you sure you want to clear ALL data?");
        alert.setContentText("This will delete the current cycle and all recorded expenses. This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            databaseManager.deleteCycle();
            if (onClearAllHandler != null) {
                onClearAllHandler.run();
            } else if (backToDashboardHandler != null) {
                backToDashboardHandler.run();
            }
        }
    }

    @FXML
    private void handleBack() {
        if (backToDashboardHandler != null) {
            backToDashboardHandler.run();
        }
    }

    /**
     * Handles the logic for changing the user's security PIN, including validation
     * of the current PIN and matching the new PIN confirmation.
     */
    @FXML
    private void handleChangePin() {
        String currentInput = currentPinField.getText();
        String newInput = newPinField.getText();
        String confirmInput = confirmPinField.getText();

        if (currentInput.isEmpty() || newInput.isEmpty() || confirmInput.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields are required.");
            return;
        }

        if (!newInput.matches("\\d+")) {
            showAlert(Alert.AlertType.ERROR, "Error", "New PIN must be numeric.");
            return;
        }

        if (!newInput.equals(confirmInput)) {
            showAlert(Alert.AlertType.ERROR, "Error", "New PIN and Confirmation do not match.");
            return;
        }

        int currentStoredPin = databaseManager.getPin();
        if (Integer.parseInt(currentInput) != currentStoredPin) {
            showAlert(Alert.AlertType.ERROR, "Error", "Incorrect current PIN.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm PIN Change");
        confirmAlert.setHeaderText("Confirm PIN Change");
        confirmAlert.setContentText("Are you sure you want to change your security PIN?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            databaseManager.updatePin(Integer.parseInt(newInput));
            showAlert(Alert.AlertType.INFORMATION, "Success", "Security PIN updated successfully.");
            currentPinField.clear();
            newPinField.clear();
            confirmPinField.clear();
        }
    }

    /**
     * Utility method to display various types of alerts to the user.
     *
     * @param type    The AlertType (Error, Information, etc.).
     * @param title   The title of the alert window.
     * @param content The message to be displayed.
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
