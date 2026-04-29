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

public class historyScreenController {
    //Linking UI
    @FXML private TableView<Expense> tableHistory;
    @FXML private TableColumn<Expense, LocalDate> colDate;
    @FXML private TableColumn<Expense, String> colCategory;
    @FXML private TableColumn<Expense, Double> colAmount;
    @FXML private TextField txtSearch;
    @FXML private ComboBox<String> comboCategory;

    private ObservableList<Expense> transactionHistory;
    private String currentFilter = "";
    private String currentSortOrder = "Date";

    @FXML
    public void initialize() {
        colDate.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        colCategory.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCategory())
        );
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        display();

        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> {
            applyFilter(newVal);
        });
    }

    public void display() {
        ArrayList<Expense> data = databaseManager.getAllExpenses();
        transactionHistory = FXCollections.observableArrayList(data);
        tableHistory.setItems(transactionHistory);
    }

    public void applyFilter(String category) {
        this.currentFilter = category;
        if (category == null || category.isEmpty()) {
            tableHistory.setItems(transactionHistory);
        } else {
            ObservableList<Expense> filtered = transactionHistory.stream()
                    .filter(e -> e.getCategory().toLowerCase().contains(category.toLowerCase()))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            tableHistory.setItems(filtered);
        }
    }

    public void applyDateRangeFilter(LocalDate startDate, LocalDate endDate) {
        ObservableList<Expense> dateFiltered = transactionHistory.stream()
                .filter(e -> !e.getTimestamp().isBefore(startDate) && !e.getTimestamp().isAfter(endDate))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        tableHistory.setItems(dateFiltered);
    }

    public void sortByDate(String order) {
        this.currentSortOrder = order;
        if (order.equalsIgnoreCase("Newest")) {
            transactionHistory.sort(Comparator.comparing(Expense::getTimestamp).reversed());
        } else {
            transactionHistory.sort(Comparator.comparing(Expense::getTimestamp));
        }
        tableHistory.refresh();
    }

    public void sortByAmount(String order) {
        this.currentSortOrder = order;
        if (order.equalsIgnoreCase("Highest")) {
            transactionHistory.sort(Comparator.comparingDouble(Expense::getAmount).reversed());
        } else {
            transactionHistory.sort(Comparator.comparingDouble(Expense::getAmount));
        }
        tableHistory.refresh();
    }

    @FXML
    public void clearFilters() {
        txtSearch.clear();
        currentFilter = "";
        display();
    }
}