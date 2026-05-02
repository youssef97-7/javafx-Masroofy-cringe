package com.masroofy.model;

import java.time.LocalDate;
//resource: https://www.w3schools.com/java/java_date.asp

import com.masroofy.model.BudgetCycle;

public class Expense {
    private double amount;
    private Category category;
    private LocalDate timestamp;
    private int id;
    private static int counter = 0;

    public Expense(double amount, Category category) {
        this.amount = amount;
        this.category = category;
        this.timestamp = BudgetCycle.getCurrentDate();
        this.id = ++counter;             //for now its fine, but its better to make a static counter or UUID to ensure uniqueness
    }

    public int getId() {
        return this.id;
    }

    public double getAmount() {
        return this.amount;
    }

    public String getCategory() {
        return this.category.getType();
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCategory(String categoryType) {
        this.category.setType(categoryType);
    }

    public LocalDate getTimestamp() {
        return this.timestamp;
    }
}
