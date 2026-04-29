package com.masroofy.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class databaseManager {

    // Database data (for now)
    private static ArrayList<Expense> allExpenses = new ArrayList<>();
    public static double totalAllowance = 5000.0;
    public static LocalDate startDate = LocalDate.of(2026,4,1);
    public static LocalDate endDate = LocalDate.of(2026,4,30);

    public static void addExpense(int amount, String cat) {
        allExpenses.add(new Expense(amount,new Category(cat)));
    }
    public static void loadCycle() { //dummy data as there is still no database
        addExpense(1500,"Entertainment");
        addExpense(1000,"Entertainment");
        addExpense(20000,"University Equipments");
        addExpense(3000,"Health insurance");
    }
    public static ArrayList<Expense> getAllExpenses() {
        return allExpenses;
    }


    public static void updateCycle(double newLimit, LocalDate newStart, LocalDate newEnd) {
        totalAllowance = newLimit;
        startDate = newStart;
        endDate = newEnd;

        ArrayList<Expense> newList = new ArrayList<>();
        for(Expense ex : allExpenses) {
            if(!ex.getTimestamp().isBefore(newStart) && !ex.getTimestamp().isAfter(newEnd)) {
                newList.add(ex);
            }
        }
        allExpenses.clear();
        allExpenses.addAll(newList);
    }
    public static void deleteCycle() {
        allExpenses.clear();
        totalAllowance = 0;
    }
}
