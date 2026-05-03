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
        double allowance = Double.parseDouble(totalAllowance.getText());
        LocalDate start = startDate.getValue();
        LocalDate end = start.plusDays(Integer.parseInt(cycleLength.getText()));


        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/masroofy/view/MainDashboardView.fxml")
        );

        Parent dashboard = loader.load();

        MainController controller = loader.getController();
        controller.initializeData(allowance, start, end);

        stage.getScene().setRoot(dashboard);
        stage.setTitle("Dashboard");

        stage.setMaximized(true);
    }
}
