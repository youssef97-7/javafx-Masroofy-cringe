package com.masroofy.controller;

import com.masroofy.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ExpenseController {
    @FXML private ComboBox<String> categoryBox;
    @FXML private Label dailyLimit;
    @FXML private Label remainingAllowance;
    @FXML  private TextField inputamount;
    //@FXML private ExpenseManager manager = new ExpenseManager();  will run when we fix the constructor

    @FXML private Button addExpenseButton;

    @FXML
    private void handleButtonClick(){
        System.out.println("Button was clicked");
    }


    @FXML
    public void initialize  () {
        categoryBox.getItems().addAll("Food", "Transportation", "Utilities", "Shopping", "Healthcare", "Other");
        categoryBox.setPromptText("Category");
        dailyLimit.setText("Daily Limit: 0.00");
        remainingAllowance.setText("Remaining Allowance: 0.00");
    }

    @FXML
    public void handle_expenses() {
        try {
            String text = inputamount.getText();
            double amount = Double.parseDouble(text);
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be +ve");
            }
            Category cat = new Category(categoryBox.getValue());
            Expense currentexpense = new Expense(amount, cat);
            //manager.addExpense(currentexpense);
            inputamount.clear();
        } catch (NumberFormatException e) {
            System.out.println("Invalid Input" + "Please enter a number");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid Amount" + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred.");
        }
    }
}
