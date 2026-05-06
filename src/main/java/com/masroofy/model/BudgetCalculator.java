package com.masroofy.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Provides utility methods to calculate budget metrics and financial status.
 * This class handles logic for determining if spending limits have been reached,
 * calculating remaining days in a cycle, and determining daily spending limits.
 *
 * @author Mahmoud Sherif
 */
public class BudgetCalculator {
    private static final int LIMIT = 80;
    // two more attributes(budgetcycle and expensemanager) to connect budget cycle and expense manager classes with budget calculator
    BudgetCycle budgetCycle;
    ExpenseManager expenseManager;

    /**
     * Constructs a BudgetCalculator linked to a specific budget cycle and expense manager.
     *
     * @param budgetCycle    The budget cycle containing date and allowance information.
     * @param expenseManager The manager tracking the user's spending data.
     */
    public BudgetCalculator(BudgetCycle budgetCycle, ExpenseManager expenseManager)
    {
        this.budgetCycle = budgetCycle;
        this.expenseManager = expenseManager;
    }

    /**
     * Determines if the current spending has reached or exceeded the predefined
     * safety threshold percentage of the total allowance.
     *
     * @return true if the percentage of spent funds is greater than or equal to the LIMIT;
     *         false otherwise or if the total allowance is zero.
     */
    public boolean CalculateIfReached()
    {
        double totalAllowance = budgetCycle.getTotalAllowance();
        double spentSoFar = expenseManager.getTotalSpent();

        if (totalAllowance <= 0) {
            return false;
        }

        double percentage = (spentSoFar/totalAllowance) * 100;
        return percentage >= LIMIT;
    }

    // change the datatype of start date and end date in the budget cycle class
    // removed the parameters of the getRemainingDays
    // el khalfia made today get the current date intialized by the constructor so
    // we can implement a forward day button
    /**
     * Calculates the number of days remaining in the current budget cycle,
     * inclusive of the current date and the end date.
     *
     * @return The number of remaining days, or 0 if the cycle has already ended.
     */
    public int getRemainingDays()
    {
        LocalDate today = budgetCycle.getCurrentDate();
        LocalDate endDate = budgetCycle.getEndDate();
        int days = (int) ChronoUnit.DAYS.between(today, endDate) + 1;
        return Math.max(days, 0);
    }

    // removed the parameters of the CalcDailyLimit
    // removed the function rollover
    /**
     * Calculates the recommended daily spending limit based on the remaining
     * allowance and the number of days left in the cycle.
     *
     * @return The calculated daily limit as a double, or 0 if no days are left
     *         or the remaining balance is exhausted.
     */
    public double CalcDailyLimit()
    {
        double totalAllowance = budgetCycle.getTotalAllowance();
        double spentSoFar = expenseManager.getTotalSpent();
        double remaining = totalAllowance - spentSoFar;
        int daysLeft = getRemainingDays();
        if (daysLeft <= 0) return 0;
        double newDailyLimit = Math.max(remaining/daysLeft, 0);
        return newDailyLimit;
    }
}
