package com.masroofy.controller;

import com.masroofy.model.Expense;
import com.masroofy.model.ExpenseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
/**
 * Controller class responsible for managing the analytics and dashboard view.
 * This class processes expense data to populate visual charts, including a
 * pie chart for category breakdowns and a bar chart for daily expense totals.
 *
 * @author Mohamed Elwan
 * @version 1.0
 */
public class AnalyticsController {
    @FXML private PieChart categoryPieChart;
    @FXML private BarChart<String, Number> dailyBarChart;

    private ExpenseManager expenseManager;

    /**
     * Injects the ExpenseManager dependency into the controller and triggers
     * the initial data loading process for the charts.
     *
     * @param expenseManager The centralized ExpenseManager containing the application's expense data.
     */
    public void setExpenseManager(ExpenseManager expenseManager) {
        this.expenseManager = expenseManager;
        loadChartData();
    }

    /**
     * Clears the current chart data and recalculates expense totals.
     * Aggregates expenses by category for the pie chart and by date for the
     * bar chart, then updates the UI elements with the newly processed data.
     */
    private void loadChartData() {
        categoryPieChart.getData().clear();
        dailyBarChart.getData().clear();
        if (expenseManager == null) return;

        List<Expense> allExpenses = expenseManager.getExpenses();
        if (allExpenses == null || allExpenses.isEmpty()) return;

        Map<String, Double> categoryTotals = new HashMap<>();
        Map<String, Double> dailyTotals = new TreeMap<>();

        for (Expense e : allExpenses) {
            String category = e.getCategory();
            String date = e.getTimestamp().toString();
            double amount = e.getAmount();

            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + amount);
            dailyTotals.put(date, dailyTotals.getOrDefault(date, 0.0) + amount);
        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        categoryPieChart.setData(pieChartData);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Daily Expenses");
        for (Map.Entry<String, Double> entry : dailyTotals.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        dailyBarChart.getData().add(series);
    }
}