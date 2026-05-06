package com.masroofy.controller;

import com.dustinredmond.fxalert.FXAlert;
import com.masroofy.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controller class responsible for managing the user interface logic
 * for adding new expenses. It captures user input for the expense amount
 * and category, validates the data, and saves it to the underlying model.
 *
 * @author Youssef Hassib & Mohamed Khalifa
 * @version 1.0
 */
public class ExpenseController {
    @FXML private ComboBox<String> categoryBox;
    @FXML  private TextField inputamount;
    @FXML private Button addExpenseButton;
    private ExpenseManager manager;

    private Runnable onUpdate;

    /**
     * Injects the ExpenseManager dependency into the controller.
     * This manager is used to store and process the newly created expenses.
     *
     * @param manager The centralized ExpenseManager for the application.
     */
    public void setManager(ExpenseManager manager){
        this.manager = manager;
    }

    /**
     * Sets a callback function to be executed whenever a new expense
     * is successfully added. This is typically used to trigger UI refreshes
     * in other parts of the application, such as updating charts or tables.
     *
     * @param onUpdate A Runnable containing the logic to execute upon a successful update.
     */
    public void setOnUpdate(Runnable onUpdate) {
        this.onUpdate = onUpdate;
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded. It populates the category drop-down
     * menu with default expense categories and sets the placeholder text.
     */
    @FXML
    public void initialize  () {
        categoryBox.getItems().addAll("Food", "Transportation", "Utilities", "Shopping", "Healthcare", "Other");
        categoryBox.setPromptText("Category");
    }

    /**
     * Resets the category drop-down menu to its default, unselected state,
     * ensuring the "Category" prompt text is displayed correctly.
     */
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

    /**
     * Handles the action of the user attempting to add a new expense.
     * Validates that both the amount and category fields are filled, and ensures
     * the amount is a valid positive number. If valid, the expense is saved;
     * otherwise, an appropriate error dialog is displayed to the user.
     *
     * @throws NumberFormatException If the amount entered cannot be parsed as a double.
     * @throws IllegalArgumentException If the parsed amount violates expense rules.
     * @throws Exception For any other unforeseen errors during the saving process.
     */
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
