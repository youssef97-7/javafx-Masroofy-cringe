package com.masroofy.model;

import java.time.LocalDate;
import java.util.*;

/**
 * Represents a specific financial budget cycle defined by a date range and a total allowance.
 * Tracks the progression of time within the cycle and maintains a list of associated expenses.
 * @author Youssef Hassib && Mohamed Khalifa
 */
public class BudgetCycle {
    private int cycleId;
    private static int Counter = 0;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalAllowance;
    private double remainingAllowance;
    private double safeDailyLimit;
    private List<Expense> expenses = new ArrayList<>();
    //adding a forward day button
    private static LocalDate currentDate;
    private int addeddays=0;

    /**
     * Initializes a new budget cycle with a unique ID, date boundaries, and a total budget.
     * Sets the initial current date to the system's current date.
     *
     * @param start     The beginning date of the budget cycle.
     * @param end       The concluding date of the budget cycle.
     * @param allowance The total amount of money allocated for this cycle.
     */
    public BudgetCycle(LocalDate start, LocalDate end, double allowance){
        this.cycleId = ++Counter;
        this.startDate = start;
        this.endDate = end;
        this.totalAllowance = allowance;
        currentDate = LocalDate.now();
    }

    // el khalfia functions for forwarding a day
    /**
     * Retrieves the current date as tracked by the application's timeline.
     *
     * @return The current LocalDate, which may be shifted forward from the actual system date.
     */
    public static LocalDate getCurrentDate(){
        return currentDate;
    }

    /**
     * Shifts the application's internal timeline forward by exactly one day.
     * Increments the count of manually added days.
     */
    public void advanceonday(){
        currentDate = currentDate.plusDays(1);
        this.addeddays++;
    }

    /**
     * Retrieves the total number of days the cycle has been manually advanced.
     *
     * @return The count of additional days added via time manipulation.
     */
    public int getAddeddays(){
        return this.addeddays;
    }



    /** @return The list of all expenses recorded within this cycle. */
    public List<Expense> getExpenses(){
        return expenses;
    }

    /** @return The unique identifier for this budget cycle. */
    public int getCycleId() {
        return this.cycleId;
    }

    /** @return The start date of the budget cycle. */
    public LocalDate getStartDate(){
        return this.startDate;
    }

    /** @return The end date of the budget cycle. */
    public LocalDate getEndDate(){
        return this.endDate;
    }

    /** @return The total initial allowance for the cycle. */
    public double getTotalAllowance(){
        return this.totalAllowance;
    }

    /** @return The remaining balance available in the cycle. */
    public double getRemainingAllowance(){
        return this.remainingAllowance;
    }

    /** @return The calculated daily spending limit deemed safe based on the initial budget. */
    public double getSafeDailyLimit(){ //not correct
        return this.safeDailyLimit;
    }

    /**
     * Updates the safe daily limit value.
     *
     * @param limit The new safe daily limit to be set.
     */
    public void getSafeDailyLimit(double limit){ //not correct

    }
}
