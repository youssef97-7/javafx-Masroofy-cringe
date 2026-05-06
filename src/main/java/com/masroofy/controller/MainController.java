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
    @FXML private Button btnToggleTheme;
    @FXML private AnchorPane rootPane;
    private boolean isDark = false;
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
        applyCurrentTheme(dashboardPane);
        contentArea.getChildren().setAll(dashboardPane);
        updateDashboard();
    }

    @FXML
    private void showSettings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/masroofy/view/SettingsView.fxml"));
            javafx.scene.Node settingsNode = loader.load();

            SettingsController settingsController = loader.getController();
            settingsController.setInitialData(budgetCycle.getStartDate(), budgetCycle.getEndDate(), budgetCycle.getTotalAllowance());
            
            settingsController.setBackToDashboardHandler(this::showDashboard);
            settingsController.setOnCycleUpdatedHandler(() -> {
                BudgetCycle updated = databaseManager.getCycleData();
                if (updated != null) {
                    initializeData(updated.getTotalAllowance(), updated.getStartDate(), updated.getEndDate());
                }
            });
            settingsController.setOnClearAllHandler(() -> {
                initializeData(0, LocalDate.now(), LocalDate.now());
                showDashboard();
            });

            applyCurrentTheme(settingsNode);

            contentArea.getChildren().setAll(settingsNode);
        } catch (Exception e) {
            System.out.println("Error loading Settings View!");
            e.printStackTrace();
        }
    }

    /**
     * Switches the main content area to display the Export view, allowing the user
     * to save their transaction history to external files.
     */
    @FXML
    private void showExport(){
        exportPane.setVisible(true);
        applyCurrentTheme(exportPane);
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
            
            applyCurrentTheme(analyticsNode);
            contentArea.getChildren().setAll(analyticsNode);
        } catch (Exception e) {
            System.out.println("Error loading Analytics View!");
            e.printStackTrace();
        }
    }

/**
 * Toggles the application's visual theme between Light Mode and Dark Mode
 * by dynamically applying or removing the corresponding CSS style class
 * from the root layout pane and the currently active content node.
 */
    @FXML
    private void handleToggleTheme() {
        isDark = !isDark;

        applyCurrentTheme(rootPane);
        btnToggleTheme.setText(isDark ? "Light Mode" : "Dark Mode");

        // Apply only to the currently visible page in the content area
        if (!contentArea.getChildren().isEmpty()) {
            applyCurrentTheme(contentArea.getChildren().get(0));
        }
    }

    /**
     * Applies the current theme class (dark) to a given node based on the isDark state.
    */
    private void applyCurrentTheme(javafx.scene.Node node) {
        if (node == null) return;
        String themeClass = "dark";
        if (isDark) {
            if (!node.getStyleClass().contains(themeClass)) {
                node.getStyleClass().add(themeClass);
            }
        } else {
            node.getStyleClass().remove(themeClass);
        }

        // Force a CSS pass to ensure variables are re-evaluated and applied
        node.applyCss();
    }
    }
