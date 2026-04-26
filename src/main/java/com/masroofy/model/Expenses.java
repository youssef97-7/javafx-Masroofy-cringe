package com.masroofy.model;

import java.time.LocalDate;      //resource: https://www.w3schools.com/java/java_date.asp

public class Expenses {
    private double amount;
    private Category category;
    private LocalDate timestamp;
    private int id;

    public Expenses(double amount, Category category){
        this.amount = amount;
        this.category = category;
        this.timestamp = LocalDate.now();
        this.id = (int)(Math.random() * 9000) + 1000;             //for now its fine, but its better to make a static counter or UUID to ensure uniqueness
    }

    public int getId(){
        return this.id;
    }

    public double getAmount(){
        return this.amount;
    }

    public String getCategory(){
        return this.category.getType();
    }

    public LocalDate getTimestamp(){
        return this.timestamp;
    }
}
