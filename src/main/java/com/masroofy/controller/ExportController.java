package com.masroofy.controller;

import com.dustinredmond.fxalert.FXAlert;
import com.masroofy.model.databaseManager;
import javafx.fxml.FXML;

public class ExportController {

    @FXML
    public void handleExportCSV() {
        databaseManager.exportFile("csv");
        showSuccessAlert("transactions.csv");
    }

    @FXML
    public void handleExportJSON() {
        databaseManager.exportFile("json");
        showSuccessAlert("transactions.json");
    }

    @FXML
    public void handleExportXML() {
        databaseManager.exportFile("xml");
        showSuccessAlert("transactions.xml");
    }

    private void showSuccessAlert(String fileName) {
        FXAlert.info().withText("Export Successful",
                        "File Saved",
                        "Your history has been saved to " + fileName + " in your project folder.")
                .show();
    }
}