package com.masroofy.controller;

import com.masroofy.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class ExpenseController {
    @FXML private ComboBox<String> categoryBox;
    @FXML  private TextField inputamount;
    @FXML private Button addExpenseButton;
    private ExpenseManager manager;

    private Runnable onUpdate;

    public void setManager(ExpenseManager manager){
        this.manager = manager;
    }

    public void setOnUpdate(Runnable onUpdate) {
        this.onUpdate = onUpdate;
    }

    @FXML
    public void initialize  () {
        categoryBox.getItems().addAll("Food", "Transportation", "Utilities", "Shopping", "Healthcare", "Other");
        categoryBox.setPromptText("Category");
    }

    private void resetComboBox() {
        categoryBox.setValue(null);
        categoryBox.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "Category" : item);
            }
        });
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
            databaseManager.addExpense(amount, cat);

            inputamount.clear();
            if (onUpdate != null) {
                onUpdate.run();
            }
            resetComboBox();
        } catch (NumberFormatException e) {
            System.out.println("Invalid Input" + "Please enter a number");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid Amount" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
