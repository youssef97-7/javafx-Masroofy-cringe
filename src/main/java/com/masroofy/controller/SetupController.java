package com.masroofy.controller;
import com.dustinredmond.fxalert.FXAlert;
import com.masroofy.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.*;
import java.time.*;

/**
 * Controller class responsible for managing the initial setup and configuration screen.
 * It captures the user's baseline financial parameters (total allowance, start date,
 * and cycle length), validates the input, and transitions the application to the
 * main dashboard upon successful setup.
 *
 * @author Youssef Hassib
 * @version 1.0
 */
public class SetupController {
    @FXML TextField totalAllowance;
    @FXML DatePicker startDate;
    @FXML TextField cycleLength;
    @FXML PasswordField pinField;
    @FXML Label pinLabel;

    private Stage stage;
    private boolean pinRequired = false;

    @FXML
    public void initialize() {
        if (databaseManager.getPin() == -1) {
            pinRequired = true;
            pinField.setVisible(true);
            pinLabel.setVisible(true);
        } else {
            pinRequired = false;
            pinField.setVisible(false);
            pinLabel.setVisible(false);
        }
    }

    /**
     * Injects the primary JavaFX Stage into the controller.
     * This is necessary so the controller can swap out the current setup scene
     * for the main dashboard scene once configuration is complete.
     *
     * @param stage The primary stage (window) of the JavaFX application.
     */
    public void setStage(Stage stage){
        this.stage = stage;
    }

    /**
     * Handles the event triggered when the user attempts to proceed past the setup screen.
     * It validates that all required fields are filled and contain correct data types.
     * If valid, it calculates the budget cycle's end date, persists this data to the database,
     * loads the Main Dashboard view, and switches the active scene.
     *
     * @throws Exception If an error occurs while locating or loading the MainDashboardView.fxml file.
     */
    @FXML
    private void handleButtonNext() throws Exception{
        try {
            String allowanceStr = totalAllowance.getText();
            String cycleStr = cycleLength.getText();
            LocalDate start = startDate.getValue();
            String pinStr = pinField.getText();

            if (allowanceStr == null || allowanceStr.isBlank() ||
                cycleStr == null || cycleStr.isBlank() ||
                start == null) {
                FXAlert.error().withText("Setup Error", "Incomplete Data",
                        "Please ensure Allowance, Cycle Length, and Start Date are all filled out!").show();
                return;
            }

            if (pinRequired && (pinStr == null || pinStr.isBlank() || !pinStr.matches("\\d+"))) {
                FXAlert.error().withText("Setup Error", "Incomplete Data",
                        "Please create a numeric Security PIN!").show();
                return;
            }

            double allowance = Double.parseDouble(allowanceStr);
            int days = Integer.parseInt(cycleStr);
            LocalDate end = start.plusDays(days);

            if (pinRequired) {
                databaseManager.updatePin(Integer.parseInt(pinStr));
            }
            
            databaseManager.updateCycle(allowance, start, end);

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/masroofy/view/MainDashboardView.fxml"));
            Parent dashboard = loader.load();

            MainController controller = loader.getController();
            controller.initializeData(allowance, start, end);
            stage.getScene().setRoot(dashboard);
            stage.setTitle("Dashboard");
            stage.setMaximized(true);

        } catch (NumberFormatException e) {
            FXAlert.error().withText("Error", "Invalid Number", "Please enter valid numeric values!").show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
