package com.masroofy.controller;

import com.dustinredmond.fxalert.FXAlert;
import com.masroofy.model.databaseManager;
import javafx.fxml.FXML;

/**
 * Controller class responsible for handling data export operations.
 * It triggers the process of saving the user's transaction history into
 * various file formats (CSV, JSON, XML) and provides visual feedback upon completion.
 *
 * @author Youssef Hassib
 * @version 1.0
 */
public class ExportController {

    /**
     * Handles the user action to export expense data to a CSV (Comma Separated Values) file.
     * Automatically displays a success notification once the file is generated.
     */
    @FXML
    public void handleExportCSV() {
        databaseManager.exportFile("csv");
        showSuccessAlert("transactions.csv");
    }

    /**
     * Handles the user action to export expense data to a JSON (JavaScript Object Notation) file.
     * Automatically displays a success notification once the file is generated.
     */
    @FXML
    public void handleExportJSON() {
        databaseManager.exportFile("json");
        showSuccessAlert("transactions.json");
    }

    /**
     * Handles the user action to export expense data to an XML (eXtensible Markup Language) file.
     * Automatically displays a success notification once the file is generated.
     */
    @FXML
    public void handleExportXML() {
        databaseManager.exportFile("xml");
        showSuccessAlert("transactions.xml");
    }

    /**
     * Displays an informational alert dialog to confirm that the export process
     * was successful, indicating the name of the newly created file.
     *
     * @param fileName The name of the exported file (e.g., "transactions.csv") to display in the alert.
     */
    private void showSuccessAlert(String fileName) {
        FXAlert.info().withText("Export Successful",
                        "File Saved",
                        "Your history has been saved to " + fileName + " in your project folder.")
                .show();
    }
}