package com.masroofy.controller;

import com.masroofy.model.*;
import javafx.fxml.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.time.*;
import java.util.ArrayList;

public class MainController {
    @FXML  Label dailyLimitLabel;
    @FXML  Label totalAllowanceLabel;
    @FXML private ExpenseController expenseViewController;
    @FXML private historyScreenController historyScreenViewController;
    @FXML private StackPane contentArea;
    @FXML private AnchorPane dashboardPane;
    @FXML private Button btnDashboard;
    @FXML private Button btnAnalytics;
    private BudgetCycle budgetCycle;
    private BudgetCalculator budgetCalculator;
    private ExpenseManager expenseManager;

    public void initializeData(double allowance, LocalDate start, LocalDate end){
        budgetCycle = new BudgetCycle(start, end, allowance);
        expenseManager = new ExpenseManager(budgetCycle);
        budgetCalculator = new BudgetCalculator(budgetCycle, expenseManager);
        expenseViewController.setManager(expenseManager);
        ArrayList<Expense> pastExpenses = databaseManager.getAllExpenses();
        for(Expense e: pastExpenses){
            expenseManager.addExpense(e);
        }
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
    @FXML
    private void showDashboard() {
        contentArea.getChildren().setAll(dashboardPane);
        updateDashboard();
    }
    @FXML private void showAnalytics() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/masroofy/view/AnalyticsView.fxml"));
            javafx.scene.Node analyticsNode = loader.load();

            AnalyticsController analyticsController = loader.getController();
            analyticsController.setExpenseManager(this.expenseManager);
            contentArea.getChildren().setAll(analyticsNode);
        } catch (Exception e) {
            System.out.println("Error loading Analytics View!");
            e.printStackTrace();
        }
    }
}
