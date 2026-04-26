package com.masroofy;

import com.masroofy.model.Category;
import com.masroofy.model.Expense;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage){
        Button btn = new Button("Say 'Hello World ya walad;");
        btn.setOnAction(event ->System.out.println("Hello World!"));

        StackPane root = new StackPane();
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Masroofy Cringe Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args){
        //launch(args);
        Category c = new Category("food");
        Expense e = new Expense(50, c);
        System.out.println("The amount spent here is " + e.getAmount());
        System.out.println("The category here is " + e.getCategory());
        System.out.println("The date of the expense used here is " + e.getTimestamp());
        System.out.println("The category of this expense is " + e.getCategory());
        System.out.println("The id here is " + e.getId());
    }
}
