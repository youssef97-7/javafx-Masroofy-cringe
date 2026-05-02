package com.masroofy.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class BudgetCalculator {
    private static final int LIMIT = 80;
    // two more attributes(budgetcycle and expensemanager) to connect budget cycle and expense manager classes with budget calculator
    BudgetCycle budgetCycle;
    ExpenseManager expenseManager;

    // added constructor with budget cycle and expense manager parameters
    public BudgetCalculator(BudgetCycle budgetCycle, ExpenseManager expenseManager)
    {
        this.budgetCycle = budgetCycle;
        this.expenseManager = expenseManager;
    }

    // removed the parameters of this function
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
    public int getRemainingDays()
    {
        LocalDate today = budgetCycle.getCurrentDate();
        LocalDate endDate = budgetCycle.getEndDate();
        int days = (int) ChronoUnit.DAYS.between(today, endDate) + 1;
        return Math.max(days, 0);
    }

    // removed the parameters of the CalcDailyLimit
    // removed the function rollover
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
