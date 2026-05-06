package com.masroofy.model;

import java.util.*;

/**
 * Orchestrates the management of expenses within a specific budget cycle.
 * Provides functionality to add, edit, delete, and filter expenses,
 * as well as calculate spending totals across various categories.
 */
public class ExpenseManager {
    private BudgetCycle currentCycle;
    private List<Expense> expenses;
    /**
     * Initializes the manager by linking it to an active budget cycle
     * and loading its existing list of expenses.
     *
     * @param cycle The BudgetCycle whose expenses are to be managed.
     */
    public ExpenseManager(BudgetCycle cycle) {
        this.currentCycle = cycle;
        this.expenses = this.currentCycle.getExpenses();
    }

    /**
     * Appends a new expense to the current tracking list.
     *
     * @param expense The Expense object to be added.
     */
    public void addExpense(Expense expense) { //should be boolean but i forgot why
        expenses.add(expense);
    }

    /**
     * Locates an existing expense by its unique ID and updates its
     * monetary amount and category classification.
     *
     * @param expenseId   The ID of the expense to modify.
     * @param newAmount   The updated cost of the expense.
     * @param newCategory The updated category label.
     */
    public void editExpense(int expenseId, double newAmount, String newCategory){ // need to be boolean
        for(int i = 0; i < expenses.size(); ++i){
            Expense current = expenses.get(i);

            if(current.getId() == expenseId){
                expenses.get(i).setAmount(newAmount);
                expenses.get(i).setCategory(newCategory);
            }
        }
    }

    /**
     * Removes an expense from the list based on its unique identifier.
     *
     * @param expenseId The ID of the expense to be permanently removed.
     */
    public void deleteExpense(int expenseId){
        expenses.removeIf( expense ->expense.getId() == expenseId);
    }

    /**
     * Filters the full expense list to find all entries matching a specific category.
     *
     * @param categoryType The name of the category to filter by.
     * @return A list of expenses belonging only to the specified category.
     */
    public List<Expense> getExpensesByCategory(String categoryType) {
        List<Expense> result = new ArrayList<Expense>();
        for(int i = 0; i < expenses.size(); ++i){
            Expense current = expenses.get(i);
            if(current.getCategory().equals(categoryType)){
                result.add(current);
            }
        }
        return result;
    }

    /**
     * Calculates the cumulative sum of all expenses within a particular category.
     *
     * @param categoryType The name of the category to sum.
     * @return The total amount spent in that category.
     */
    public double getTotalSpentOnCategory(String categoryType){
        double result = 0.0;
        for(int i = 0; i < expenses.size(); ++i){
            Expense current = expenses.get(i);
            if(current.getCategory().equals(categoryType)){
                result += current.getAmount();
            }
        }
        return result;
    }

    /**
     * Calculates the total expenditure across all categories in the current cycle.
     *
     * @return The sum of all recorded expense amounts.
     */
    public double getTotalSpent(){
        double result = 0.0;
        for(int i = 0; i < expenses.size(); ++i){
            Expense current = expenses.get(i);
            result += expenses.get(i).getAmount();
        }
        return result;

    }

    /**
     * Retrieves the complete list of managed expenses.
     *
     * @return The current list of Expense objects.
     */
    public List<Expense> getExpenses(){
        return expenses;
    }
}
