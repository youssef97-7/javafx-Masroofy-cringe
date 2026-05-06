package com.masroofy.controller;

import com.masroofy.model.Expense;
import com.masroofy.model.ExpenseManager;
import com.masroofy.model.databaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller class responsible for managing the transaction history screen.
 * Handles display, filtering, sorting, editing, and deleting of expense records.
 *
 * @author Mohamed Elwan
 * @version 2.0
 */
public class historyScreenController {

    // ── FXML bindings ────────────────────────────────────────────────────────
    @FXML private TableView<Expense>              tableHistory;
    @FXML private TableColumn<Expense, LocalDate> colDate;
    @FXML private TableColumn<Expense, String>    colCategory;
    @FXML private TableColumn<Expense, Double>    colAmount;
    @FXML private TableColumn<Expense, Void>      colActions;   // new column
    @FXML private TextField                        txtSearch;
    @FXML private ComboBox<String>                 comboCategory;

    // ── State ────────────────────────────────────────────────────────────────
    public  ObservableList<Expense> transactionHistory;
    private String  currentFilter    = "";
    private String  currentSortOrder = "Date";

    /** Injected from MainController so edits/deletes can refresh the dashboard */
    private ExpenseManager expenseManager;
    /** Called after any edit or delete to refresh the dashboard totals */
    private Runnable onUpdate;

    // ── Dependency injection ─────────────────────────────────────────────────

    public void setExpenseManager(ExpenseManager manager) {
        this.expenseManager = manager;
    }

    public void setOnUpdate(Runnable onUpdate) {
        this.onUpdate = onUpdate;
    }

    // ── Initialization ───────────────────────────────────────────────────────

    @FXML
    public void initialize() {
        comboCategory.getItems().addAll(
                "Food", "Transportation", "Utilities", "Shopping", "Healthcare", "Other");
        comboCategory.setOnAction(e -> applyFilter(comboCategory.getValue()));

        colDate.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        colCategory.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getCategory()));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        setupActionsColumn();
        display();
    }

    /**
     * Builds the Actions column with an Edit button and a Delete button per row.
     * Uses a custom cell factory so each row gets its own pair of buttons.
     */
    private void setupActionsColumn() {
        colActions.setCellFactory(col -> new TableCell<>() {

            // Two small buttons created once per cell
            private final Button btnEdit   = new Button("✏");
            private final Button btnDelete = new Button("🗑");
            private final HBox   box       = new HBox(6, btnEdit, btnDelete);

            // Style the buttons inline so they work regardless of theme
            {
                box.setAlignment(Pos.CENTER);

                btnEdit.setStyle(
                        "-fx-background-color: transparent;" +
                                "-fx-border-color: #2f8a5f;" +
                                "-fx-border-radius: 4px;" +
                                "-fx-background-radius: 4px;" +
                                "-fx-text-fill: #2f8a5f;" +
                                "-fx-font-size: 13px;" +
                                "-fx-cursor: hand;" +
                                "-fx-padding: 2px 7px;"
                );
                btnEdit.setOnMouseEntered(e -> btnEdit.setStyle(
                        "-fx-background-color: #2f8a5f;" +
                                "-fx-border-color: #2f8a5f;" +
                                "-fx-border-radius: 4px;" +
                                "-fx-background-radius: 4px;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 13px;" +
                                "-fx-cursor: hand;" +
                                "-fx-padding: 2px 7px;"
                ));
                btnEdit.setOnMouseExited(e -> btnEdit.setStyle(
                        "-fx-background-color: transparent;" +
                                "-fx-border-color: #2f8a5f;" +
                                "-fx-border-radius: 4px;" +
                                "-fx-background-radius: 4px;" +
                                "-fx-text-fill: #2f8a5f;" +
                                "-fx-font-size: 13px;" +
                                "-fx-cursor: hand;" +
                                "-fx-padding: 2px 7px;"
                ));

                btnDelete.setStyle(
                        "-fx-background-color: transparent;" +
                                "-fx-border-color: #e03e3e;" +
                                "-fx-border-radius: 4px;" +
                                "-fx-background-radius: 4px;" +
                                "-fx-text-fill: #e03e3e;" +
                                "-fx-font-size: 13px;" +
                                "-fx-cursor: hand;" +
                                "-fx-padding: 2px 7px;"
                );
                btnDelete.setOnMouseEntered(e -> btnDelete.setStyle(
                        "-fx-background-color: #e03e3e;" +
                                "-fx-border-color: #e03e3e;" +
                                "-fx-border-radius: 4px;" +
                                "-fx-background-radius: 4px;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 13px;" +
                                "-fx-cursor: hand;" +
                                "-fx-padding: 2px 7px;"
                ));
                btnDelete.setOnMouseExited(e -> btnDelete.setStyle(
                        "-fx-background-color: transparent;" +
                                "-fx-border-color: #e03e3e;" +
                                "-fx-border-radius: 4px;" +
                                "-fx-background-radius: 4px;" +
                                "-fx-text-fill: #e03e3e;" +
                                "-fx-font-size: 13px;" +
                                "-fx-cursor: hand;" +
                                "-fx-padding: 2px 7px;"
                ));

                btnEdit.setOnAction(e -> {
                    Expense expense = getTableView().getItems().get(getIndex());
                    handleEdit(expense);
                });

                btnDelete.setOnAction(e -> {
                    Expense expense = getTableView().getItems().get(getIndex());
                    handleDelete(expense);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    // ── Edit & Delete handlers ───────────────────────────────────────────────

    /**
     * Opens a dialog pre-filled with the selected expense's data.
     * If the user confirms, updates both the in-memory model and the database.
     */
    private void handleEdit(Expense expense) {
        // Amount field
        TextField amountField = new TextField(String.valueOf(expense.getAmount()));
        amountField.setPromptText("Amount");

        // Category dropdown
        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(
                "Food", "Transportation", "Utilities", "Shopping", "Healthcare", "Other");
        categoryBox.setValue(expense.getCategory());
        categoryBox.setMaxWidth(Double.MAX_VALUE);

        // Layout
        javafx.scene.layout.VBox content = new javafx.scene.layout.VBox(10,
                new Label("Amount:"),   amountField,
                new Label("Category:"), categoryBox
        );
        content.setPadding(new javafx.geometry.Insets(10));

        // Dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Expense");
        dialog.setHeaderText("Edit expense #" + expense.getId());
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                double newAmount = Double.parseDouble(amountField.getText().trim());
                String newCategory = categoryBox.getValue();

                if (newAmount <= 0) {
                    showError("Amount must be a positive number.");
                    return;
                }
                if (newCategory == null || newCategory.isEmpty()) {
                    showError("Please select a category.");
                    return;
                }

                // Update in-memory model
                if (expenseManager != null) {
                    expenseManager.editExpense(expense.getId(), newAmount, newCategory);
                }

                // Update database
                databaseManager.updateExpense(expense.getId(), newAmount, newCategory);

                // Refresh table and dashboard
                display();
                if (onUpdate != null) onUpdate.run();

            } catch (NumberFormatException ex) {
                showError("Please enter a valid number for the amount.");
            }
        }
    }

    /**
     * Asks the user to confirm deletion, then removes the expense from
     * both the in-memory model and the database.
     */
    private void handleDelete(Expense expense) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Expense");
        confirm.setHeaderText("Delete this expense?");
        confirm.setContentText(
                "Category: " + expense.getCategory() +
                        "\nAmount: EGP" + expense.getAmount() +
                        "\nDate: " + expense.getTimestamp() +
                        "\n\nThis cannot be undone.");

        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Remove from in-memory model
            if (expenseManager != null) {
                expenseManager.deleteExpense(expense.getId());
            }

            // Remove from database
            databaseManager.deleteExpense(expense.getId());

            // Refresh table and dashboard
            display();
            if (onUpdate != null) onUpdate.run();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid Input");
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ── Display & Filtering ──────────────────────────────────────────────────

    public void display() {
        ArrayList<Expense> data = databaseManager.getAllExpenses();
        transactionHistory = FXCollections.observableArrayList(data);
        tableHistory.setItems(transactionHistory);
    }

    public void applyFilter(String category) {
        this.currentFilter = category;
        if (category == null || category.equals("Category") || category.isEmpty()) {
            tableHistory.setItems(transactionHistory);
        } else {
            ObservableList<Expense> filtered = transactionHistory.stream()
                    .filter(e -> e.getCategory().toLowerCase()
                            .contains(category.toLowerCase()))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            tableHistory.setItems(filtered);
        }
    }

    public void applyDateRangeFilter(LocalDate startDate, LocalDate endDate) {
        ObservableList<Expense> dateFiltered = transactionHistory.stream()
                .filter(e -> !e.getTimestamp().isBefore(startDate)
                        && !e.getTimestamp().isAfter(endDate))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        tableHistory.setItems(dateFiltered);
    }

    public void sortByDate(String order) {
        this.currentSortOrder = order;
        if (order.equalsIgnoreCase("Newest"))
            transactionHistory.sort(Comparator.comparing(Expense::getTimestamp).reversed());
        else
            transactionHistory.sort(Comparator.comparing(Expense::getTimestamp));
        tableHistory.refresh();
    }

    public void sortByAmount(String order) {
        this.currentSortOrder = order;
        if (order.equalsIgnoreCase("Highest"))
            transactionHistory.sort(Comparator.comparingDouble(Expense::getAmount).reversed());
        else
            transactionHistory.sort(Comparator.comparingDouble(Expense::getAmount));
        tableHistory.refresh();
    }

    private void resetComboBox() {
        comboCategory.setValue(null);
        comboCategory.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "Category" : item);
            }
        });
    }

    @FXML
    public void clearFilters() {
        currentFilter = "Category";
        resetComboBox();
        display();
    }
}
