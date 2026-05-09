package com.masroofy;

import com.masroofy.controller.MainController;
import com.masroofy.controller.PinEntryController;
import com.masroofy.controller.SetupController;
import com.masroofy.model.BudgetCycle;
import com.masroofy.model.Category;
import com.masroofy.model.Expense;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;
import javafx.scene.image.Image;

import com.masroofy.model.databaseManager;

/**
 * The main entry point for the Masroofy JavaFX application.
 * This class orchestrates the application lifecycle, including database initialization
 * and the conditional loading of either the setup wizard or the main dashboard.
 */
public class MainApp extends Application {


    /**
     * Loads and displays the setup scene where the user can configure a new budget cycle.
     *
     * @param primaryStage The primary window of the application.
     * @throws Exception   If the FXML file for the setup view cannot be loaded.
     */
    private void loadSetup(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/SetupView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene (root, 1200, 700);

        SetupController controller = loader.getController();
        controller.setStage(primaryStage);
        primaryStage.setTitle("Masroofy Setup");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Loads and displays the main dashboard scene using data from an existing budget cycle.
     *
     * @param primaryStage The primary window of the application.
     * @param savedCycle   The BudgetCycle object containing the user's saved financial data.
     * @throws Exception   If the FXML file for the dashboard view cannot be loaded.
     */
    private void loadDashboard(Stage primaryStage, BudgetCycle savedCycle) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/MainDashboardView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        MainController controller = loader.getController();

        controller.initializeData(savedCycle.getTotalAllowance(), savedCycle.getStartDate(), savedCycle.getEndDate());

        primaryStage.setTitle("Masroofy Dashboard");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }


    /**
     * Initializes the database and determines the application's starting state.
     * If a previous session is detected, it prompts the user to either resume
     * the old session or wipe the data and start a new one.
     *
     * @param primaryStage The primary stage for this application.
     * @throws Exception   If there is an error during scene transitions.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Image appIcon= new Image(getClass().getResourceAsStream("view/masroofy.png"));
        primaryStage.getIcons().add(appIcon);
        databaseManager.initializeDatabase();
        
        BudgetCycle saved = databaseManager.getCycleData();
        if (saved == null) {
            // No budget cycle exists - Go to Setup (which handles PIN creation if needed)
            loadSetup(primaryStage);
        } else {
            // Budget cycle found - Returning user, must authenticate first
            showPinEntry(primaryStage);
        }
    }

    private void showPinEntry(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/PinEntryView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 400, 450);

        PinEntryController controller = loader.getController();
        controller.setStage(primaryStage);
        controller.setOnSuccess(() -> {
            try {
                proceedAfterAuth(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        primaryStage.setTitle("Masroofy - Authentication");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private void proceedAfterAuth(Stage primaryStage) throws Exception {
        BudgetCycle saved = databaseManager.getCycleData();
        if(saved!= null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Session Found");
            alert.setHeaderText("Previous Session was Found");
            alert.setContentText("Do you want to load your old session or start a pure one? ");

            ButtonType load = new ButtonType("Load Session");
            ButtonType pure = new ButtonType("New Session");
            alert.getButtonTypes().setAll(load, pure);

            Optional<ButtonType> res = alert.showAndWait();
            if(res.isPresent() && res.get() == load){
                loadDashboard(primaryStage, saved);
            }else{
                databaseManager.deleteCycle();
                loadSetup(primaryStage);
            }
        }else{
            loadSetup(primaryStage);
        }
    }


    /**
     * The standard Java main method used to launch the JavaFX application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args){
        launch(args);
    }
}