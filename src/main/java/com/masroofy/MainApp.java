package com.masroofy;

import com.masroofy.controller.MainController;
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

import com.masroofy.model.databaseManager;

public class MainApp extends Application {


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


    @Override
    public void start(Stage primaryStage) throws Exception {

        databaseManager.initializeDatabase();
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


    public static void main(String[] args){
        launch(args);
    }
}