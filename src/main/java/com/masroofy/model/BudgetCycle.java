package com.masroofy.model;

import java.time.LocalDate;
import java.util.*;
public class BudgetCycle {
    private int cycleId;
    private static int Counter = 0;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalAllowance;
    private double remainingAllowance;
    private double safeDailyLimit;
    private List<Expense> expenses = new ArrayList<>();

    public BudgetCycle(LocalDate start, LocalDate end, double allowance){
        this.cycleId = ++Counter;
        this.startDate = start;
        this.endDate = end;
        this.totalAllowance = allowance;
    }

    public List<Expense> getExpenses(){
        return expenses;
    }

    public int getCycleId() {
        return this.cycleId;
    }

    public LocalDate getStartDate(){
        return this.startDate;
    }

    public LocalDate getEndDate(){
        return this.endDate;
    }

    public double getTotalAllowance(){
        return this.totalAllowance;
    }

    public double getRemainingAllowance(){
        return this.remainingAllowance;
    }

    public double getSafeDailyLimit(){ //not correct
        return this.safeDailyLimit;
    }

    public void getSafeDailyLimit(double limit){ //not correct

    }
}
