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

/**
 * The primary controller for the application's main window.
 * It acts as the central hub, managing navigation between different views
 * (Dashboard, Analytics, Export) and initializing the core data models
 * and sub-controllers.
 *
 * @author Mohamed Elwan, Mahmoud Sherif, Youssef Hassib, Mohamed Khalifa
 * @version 1.0
 */
public class MainController {
    @FXML  Label dailyLimitLabel;
    @FXML  Label totalAllowanceLabel;
    @FXML private ExpenseController expenseViewController;
    @FXML private historyScreenController historyScreenViewController;
    @FXML private StackPane contentArea;
    @FXML private AnchorPane dashboardPane;
    @FXML private Button btnDashboard;
    @FXML private Button btnAnalytics;
    @FXML private AnchorPane exportPane;
    private BudgetCycle budgetCycle;
    private BudgetCalculator budgetCalculator;
    private ExpenseManager expenseManager;

    /**
     * Initializes the core application data and sets up the dependencies between
     * models and controllers. It also loads existing expense history from the database
     * and establishes the UI update loop for the dashboard.
     *
     * @param allowance The total budget allowance for the current cycle.
     * @param start     The start date of the budget cycle.
     * @param end       The end date of the budget cycle.
     */
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

    /**
     * Recalculates the user's financial metrics (daily limit and remaining allowance)
     * and updates the corresponding summary labels on the dashboard UI.
     */
    private void updateDashboard(){
        double dailyLimit = budgetCalculator.CalcDailyLimit();
        double totalSpent = expenseManager.getTotalSpent();
        double remainingAllowance = budgetCycle.getTotalAllowance() - totalSpent;

        dailyLimitLabel.setText(String.format("Daily Limit: EGP%.2f", dailyLimit));
        totalAllowanceLabel.setText(String.format("Total Allowance: EGP%.2f", remainingAllowance));
    }

    /**
     * A simulation utility that advances the application's internal budget cycle date
     * by one day. This is used to test and trigger budget recalculations over time.
     */
    @FXML
    private void handleforwardoneday(){
        if(budgetCycle != null){
            budgetCycle.advanceonday();
            updateDashboard();
            System.out.println("Time Travel! New simulated date: " + budgetCycle.getCurrentDate());
        }
    }

    /**
     * Switches the main content area to display the Dashboard view and refreshes
     * its data to ensure metrics are up-to-date.
     */
    @FXML
    private void showDashboard() {
        contentArea.getChildren().setAll(dashboardPane);
        updateDashboard();
    }
    /**
     * Switches the main content area to display the Export view, allowing the user
     * to save their transaction history to external files.
     */
    @FXML
    private void showExport(){
        exportPane.setVisible(true);
        contentArea.getChildren().setAll(exportPane);
        updateDashboard();
    }
    /**
     * Dynamically loads the Analytics view from its FXML file, injects the necessary
     * data models (ExpenseManager) into its controller, and switches the main content
     * area to display the charts.
     */
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
    @FXML private Button btnToggleTheme;
    @FXML private AnchorPane rootPane;
    private boolean isDark = false;

    /**
     * Toggles the application's visual theme between Light Mode and Dark Mode
     * by dynamically applying or removing the corresponding CSS style class
     * from the root layout pane.
     */
    @FXML
    private void handleToggleTheme() {
        isDark = !isDark;
        if (isDark) {
            rootPane.getStyleClass().add("dark");
            btnToggleTheme.setText("Light Mode");
        } else {
            rootPane.getStyleClass().remove("dark");
            btnToggleTheme.setText("Dark Mode");
        }
    }
}
