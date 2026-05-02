package com.masroofy.controller;

import com.masroofy.model.BudgetCalculator;
import com.masroofy.model.BudgetCycle;
import com.masroofy.model.ExpenseManager;
import javafx.fxml.*;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.time.*;

public class MainController {
    @FXML  Label dailyLimitLabel;
    @FXML  Label totalAllowanceLabel;
    @FXML private ExpenseController expenseViewController;
    @FXML private historyScreenController historyScreenViewController;
    private BudgetCycle budgetCycle;
    private BudgetCalculator budgetCalculator;
    private ExpenseManager expenseManager;

    public void initializeData(double allowance, LocalDate start, LocalDate end){
        budgetCycle = new BudgetCycle(start, end, allowance);
        expenseManager = new ExpenseManager(budgetCycle);
        budgetCalculator = new BudgetCalculator(budgetCycle, expenseManager);
        expenseViewController.setManager(expenseManager);
        expenseViewController.setOnUpdate(() -> {
            historyScreenViewController.display();
            updateDashboard();
        });
        updateDashboard();
    }

    private void updateDashboard(){
        double dailyLimit = budgetCalculator.CalcDailyLimit();
        double totalSpent = expenseManager.getTotalSpent();
        double remainingAllowance = budgetCycle.getTotalAllowance() - totalSpent;

        dailyLimitLabel.setText(String.format("Daily Limit: EGP%.2f", dailyLimit));
        totalAllowanceLabel.setText(String.format("Total Allowance: EGP%.2f", remainingAllowance));
    }

    @FXML
    private void handleforwardoneday(){
        if(budgetCycle != null){
            budgetCycle.advanceonday();
            updateDashboard();
            System.out.println("Time Travel! New simulated date: " + budgetCycle.getCurrentDate());
        }
    }
}
