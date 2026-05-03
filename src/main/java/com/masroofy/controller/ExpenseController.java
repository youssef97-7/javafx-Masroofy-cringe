package com.masroofy.controller;

import com.dustinredmond.fxalert.FXAlert;
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
           int count = 0;
           String text = inputamount.getText();
           String catValue = categoryBox.getValue();

           if(text!= null && !text.isEmpty()) count++;
           if(catValue!= null)count++;

           if(count == 2){
               double amount = Double.parseDouble(text);
               if(amount <= 0){
                   FXAlert.error().withText("Error", "Invalid Amount", "Amount must be a positive number.").show();
               }
               Category cat = new Category(catValue);
               Expense currentexpense = new Expense(amount, cat);

               manager.addExpense(currentexpense);
               databaseManager.addExpense(currentexpense);
               inputamount.clear();
               if (onUpdate != null) {
                   onUpdate.run();
               }
               resetComboBox();
           }else{
               FXAlert.error().withText("Error", "Invalid Inputs",
                       "Please fill in both input fields to continue <3").show();
           }
        } catch (NumberFormatException e) {
            FXAlert.error().withText("Error", "Invalid Inputs",
                    "Please enter a number").show();
        } catch (IllegalArgumentException e) {
            FXAlert.error().withText("Error", "Invalid Inputs",
                    "Invalid Amount").show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
