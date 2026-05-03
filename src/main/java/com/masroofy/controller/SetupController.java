package com.masroofy.controller;
import com.dustinredmond.fxalert.FXAlert;
import com.masroofy.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.*;
import java.time.*;

public class SetupController {
    @FXML TextField totalAllowance;
    @FXML DatePicker startDate;
    @FXML TextField cycleLength;


    private Stage stage;

    public void setStage(Stage stage){
        this.stage = stage;
    }

    @FXML
    private void handleButtonNext() throws Exception{
        try {
            int count = 0;
            String allowanceStr = totalAllowance.getText();
            String cycleStr = cycleLength.getText();
            LocalDate start = startDate.getValue();

            if (allowanceStr != null && !allowanceStr.isBlank()) count++;
            if (cycleStr != null && !cycleStr.isBlank()) count++;
            if (start != null) count++;

            if (count == 3) {
                double allowance = Double.parseDouble(allowanceStr);
                int days = Integer.parseInt(cycleStr);
                LocalDate end = start.plusDays(days);

                databaseManager.updateCycle(allowance, start, end);

                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/masroofy/view/MainDashboardView.fxml"));
                Parent dashboard = loader.load();

                MainController controller = loader.getController();
                controller.initializeData(allowance, start, end);
                stage.getScene().setRoot(dashboard);
                stage.setTitle("Dashboard");
                stage.setMaximized(true);
            } else {
                FXAlert.error().withText("Setup Error", "Incomplete Data",
                        "Please ensure Allowance, Cycle Length, and Start Date are all filled out!").show();
            }

        } catch (NumberFormatException e) {
            FXAlert.error().withText("Error", "Invalid Number", "Please enter valid numeric values for allowance and cycle length.").show();
        } catch (Exception e) {
            // Handle FXML loading errors
            e.printStackTrace();
        }
    }
}
