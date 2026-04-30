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
    private BudgetCycle budgetCycle;
    private BudgetCalculator budgetCalculator;

    public void initializeData(double allowance, LocalDate start, LocalDate end){
        budgetCycle = new BudgetCycle(start, end, allowance);
        ExpenseManager manager = new ExpenseManager(budgetCycle);
        budgetCalculator = new BudgetCalculator(budgetCycle, manager);
        expenseViewController.setManager(manager);
        updateDashboard();
    }

    private void updateDashboard(){
        double dailyLimit = budgetCalculator.CalcDailyLimit();
        double totalAllowance = budgetCycle.getTotalAllowance();

        dailyLimitLabel.setText(String.format("Daily Limit: EGP%.2f", dailyLimit));
        totalAllowanceLabel.setText(String.format("Total Allowance: EGP%.2f", totalAllowance));
    }

}
