package com.masroofy.model;

import java.time.LocalDate;
//resource: https://www.w3schools.com/java/java_date.asp

import com.masroofy.model.BudgetCycle;

/**
 * Represents an individual financial transaction within the application.
 * Each expense stores a monetary value, a classification category, and
 * the date it was recorded.
 */
public class Expense {
    private double amount;
    private Category category;
    private LocalDate timestamp;
    private int id;
    private static int counter = 0;

    /**
     * Creates a new expense with a specific amount and category.
     * The timestamp is automatically set to the current date of the budget cycle,
     * and a unique ID is assigned via an internal counter.
     *
     * @param amount   The monetary value of the expense.
     * @param category The category object defining the type of expense.
     */
    public Expense(double amount, Category category) {
        this.amount = amount;
        this.category = category;
        this.timestamp = BudgetCycle.getCurrentDate();
        this.id = ++counter;             //for now its fine, but its better to make a static counter or UUID to ensure uniqueness
    }

    /**
     * Manually updates the timestamp for this expense.
     * @param ld The new LocalDate to be assigned.
     */
    public void setTimeStamp(LocalDate ld){
        this.timestamp = ld;
    }

    /** @return The unique identifier for this specific expense. */
    public int getId() {
        return this.id;
    }

    /** @return The monetary amount of the expense. */
    public double getAmount() {
        return this.amount;
    }

    /** @return The name of the category associated with this expense. */
    public String getCategory() {
        return this.category.getType();
    }

    /**
     * Updates the monetary value of the expense.
     * @param amount The new amount to set.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Updates the category label for this expense.
     * @param categoryType The new category name as a String.
     */
    public void setCategory(String categoryType) {
        this.category.setType(categoryType);
    }

    /** @return The LocalDate when the expense was recorded. */
    public LocalDate getTimestamp() {
        return this.timestamp;
    }
}
