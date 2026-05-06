package com.masroofy.controller;

import com.masroofy.model.BudgetCycle;
import com.masroofy.model.databaseManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.util.Optional;

public class SettingsController {

    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField allowanceField;

    private Runnable backToDashboardHandler;
    private Runnable onCycleUpdatedHandler;
    private Runnable onClearAllHandler;

    public void setBackToDashboardHandler(Runnable handler) {
        this.backToDashboardHandler = handler;
    }

    public void setOnCycleUpdatedHandler(Runnable handler) {
        this.onCycleUpdatedHandler = handler;
    }

    public void setOnClearAllHandler(Runnable handler) {
        this.onClearAllHandler = handler;
    }

    public void setInitialData(LocalDate start, LocalDate end, double allowance) {
        startDatePicker.setValue(start);
        endDatePicker.setValue(end);
        allowanceField.setText(String.valueOf(allowance));
    }

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

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
