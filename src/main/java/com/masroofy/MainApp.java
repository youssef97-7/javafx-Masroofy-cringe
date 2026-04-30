package com.masroofy;

import com.masroofy.controller.SetupController;
import com.masroofy.model.Category;
import com.masroofy.model.Expense;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/SetupView.fxml"));
        Parent root = loader.load(); //
        Scene scene = new Scene(root, 600, 400); //
        SetupController controller = loader.getController();
        controller.setStage(primaryStage);

        primaryStage.setTitle("Masroofy Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}