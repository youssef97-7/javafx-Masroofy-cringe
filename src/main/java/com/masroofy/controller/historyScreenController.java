package com.masroofy.controller;

import com.masroofy.model.Expense;
import com.masroofy.model.databaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Controller class responsible for managing the transaction history screen.
 * It handles the display of all expense records in a tabular format and provides
 * functionalities for filtering (by category or date range) and sorting (by date or amount).
 *
 * @author Mohamed Elwan
 * @version 1.0
 */
public class historyScreenController {
    //Linking UI
    @FXML private TableView<Expense> tableHistory;
    @FXML private TableColumn<Expense, LocalDate> colDate;
    @FXML private TableColumn<Expense, String> colCategory;
    @FXML private TableColumn<Expense, Double> colAmount;
    @FXML private TextField txtSearch;
    @FXML private ComboBox<String> comboCategory;

    public ObservableList<Expense> transactionHistory;
    private String currentFilter = "";
    private String currentSortOrder = "Date";

    /**
     * Initializes the controller after its root element has been completely processed.
     * This method sets up the category filter dropdown, binds the table columns to the
     * respective properties of the Expense objects, and loads the initial data into the table.
     */
    @FXML
    public void initialize() {
        comboCategory.getItems().addAll("Food", "Transportation", "Utilities", "Shopping", "Healthcare", "Other");
        comboCategory.setOnAction(e -> applyFilter(comboCategory.getValue()));
        colDate.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        colCategory.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCategory())
        );
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        display();

    }

    /**
     * Retrieves all expense records from the database and populates the TableView.
     * This acts as a global refresh for the history screen.
     */
    public void display() {
        ArrayList<Expense> data = databaseManager.getAllExpenses();
        transactionHistory = FXCollections.observableArrayList(data);
        tableHistory.setItems(transactionHistory);
    }

    /**
     * Filters the displayed transaction history based on the selected category.
     * If no valid category is selected, it resets the view to show all transactions.
     *
     * @param category The name of the category to filter by (e.g., "Food", "Utilities").
     */
    public void applyFilter(String category) {
        this.currentFilter = category;
        if (category == null || category.equals("Category") || category.isEmpty()) {
            tableHistory.setItems(transactionHistory);
        } else {
            ObservableList<Expense> filtered = transactionHistory.stream()
                    .filter(e -> e.getCategory().toLowerCase().contains(category.toLowerCase()))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            tableHistory.setItems(filtered);
        }
    }

    /**
     * Filters the displayed transaction history to only show expenses that occurred
     * within a specific date range (inclusive).
     *
     * @param startDate The beginning date of the filter range.
     * @param endDate   The ending date of the filter range.
     */
    public void applyDateRangeFilter(LocalDate startDate, LocalDate endDate) {
        ObservableList<Expense> dateFiltered = transactionHistory.stream()
                .filter(e -> !e.getTimestamp().isBefore(startDate) && !e.getTimestamp().isAfter(endDate))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        tableHistory.setItems(dateFiltered);
    }

    /**
     * Sorts the transaction history table based on the date the expenses occurred.
     *
     * @param order A string indicating the sort direction. Passing "Newest" sorts
     *              descending (newest first); any other value sorts ascending (oldest first).
     */
    public void sortByDate(String order) {
        this.currentSortOrder = order;
        if (order.equalsIgnoreCase("Newest")) {
            transactionHistory.sort(Comparator.comparing(Expense::getTimestamp).reversed());
        } else {
            transactionHistory.sort(Comparator.comparing(Expense::getTimestamp));
        }
        tableHistory.refresh();
    }

    /**
     * Sorts the transaction history table based on the monetary amount of the expenses.
     *
     * @param order A string indicating the sort direction. Passing "Highest" sorts
     *              descending (largest amount first); any other value sorts ascending.
     */
    public void sortByAmount(String order) {
        this.currentSortOrder = order;
        if (order.equalsIgnoreCase("Highest")) {
            transactionHistory.sort(Comparator.comparingDouble(Expense::getAmount).reversed());
        } else {
            transactionHistory.sort(Comparator.comparingDouble(Expense::getAmount));
        }
        tableHistory.refresh();
    }

    /**
     * Resets the category filter dropdown to its default, unselected state,
     * restoring the placeholder text.
     */
    private void resetComboBox() {
        comboCategory.setValue(null);
        comboCategory.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "Category" : item);
            }
        });
    }

    /**
     * Clears all currently active filters and sorting criteria, resets the UI controls,
     * and reloads the full, unfiltered transaction history into the table.
     */
    @FXML
    public void clearFilters() {
        currentFilter = "Category";
        resetComboBox();
        display();
    }
}