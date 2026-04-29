package com.masroofy.controller;

import com.masroofy.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ExpenseController {
    @FXML
    private ComboBox<String> categoryBox;
    private TextArea dailyLimit;
    private TextArea remainingAllowance;
    private TextField inputamount;
    private ExpenseManager manager;
    private ComboBox<String> inputcat;


    @FXML
    public void initial() {
        categoryBox.getItems().addAll("Food", "Transportation", "Utilities", "Shopping", "Healthcare", "Other");
        categoryBox.setPromptText("Category");
        dailyLimit.setText("daily limit : 0.00");
        remainingAllowance.setText("remainingAllowance : 0.00");

    }

    @FXML
    public void handle_expenses() {
        try {
            String text = inputamount.getText();
            double amount = Double.parseDouble(text);
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be +ve");
            }
            Category cat = new Category(inputcat.getValue());
            Expense currentexpense = new Expense(amount, cat);
            manager.addExpense(currentexpense);
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
