package com.masroofy.model;

      //resource: https://www.w3schools.com/java/java_date.asp
import java.time.*;

public class Expense {
    private double amount;
    private Category category;
    private LocalDate timestamp;
    private static int counter = 0;
    private int id;

    public Expense(double amount, Category category){
        this.amount = amount;
        this.category = category;
        this.timestamp = LocalDate.now();
        this.id = ++counter;
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

    public void setAmount(double amount){
        this.amount = amount;
    }

    public void setCategory(String type){
        this.category.setType(type);
    }

    public LocalDate getTimestamp(){

        return this.timestamp;
    }

}
