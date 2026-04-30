package com.masroofy.controller;

import com.masroofy.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import static com.masroofy.controller.historyScreenController.display;
import static com.masroofy.model.databaseManager.addExpense;

public class ExpenseController {
    @FXML private ComboBox<String> categoryBox;
    @FXML  private TextField inputamount;
    @FXML private Button addExpenseButton;
    private ExpenseManager manager;


    public void setManager(ExpenseManager manager){
        this.manager = manager;
    }
    @FXML
    public void initialize  () {
        categoryBox.getItems().addAll("Food", "Transportation", "Utilities", "Shopping", "Healthcare", "Other");
        categoryBox.setPromptText("Category");
    }

    @FXML
    public void handleAddExpense() {
        try {
            String text = inputamount.getText();
            double amount = Double.parseDouble(text);
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be +ve");
            }
            Category cat = new Category(categoryBox.getValue());
            Expense currentexpense = new Expense(amount, cat);
            manager.addExpense(currentexpense);
            addExpense(amount,cat);
            inputamount.clear();
            categoryBox.setValue(null);
            display();
        } catch (NumberFormatException e) {
            System.out.println("Invalid Input" + "Please enter a number");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid Amount" + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred.");
        }
    }
}
