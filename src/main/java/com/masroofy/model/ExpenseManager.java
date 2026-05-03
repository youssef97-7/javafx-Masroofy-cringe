package com.masroofy.model;

import java.util.*;
//hello github
public class ExpenseManager {
    private BudgetCycle currentCycle;
    private List<Expense> expenses;
    public ExpenseManager(BudgetCycle cycle) { // won't run unless we empty the constructor cause we  don't have a budgetcycle yet
        this.currentCycle = cycle;
        this.expenses = this.currentCycle.getExpenses();
    }

    public void addExpense(Expense expense) { //should be boolean but i forgot why
        expenses.add(expense);
    }

    public void editExpense(int expenseId, double newAmount, String newCategory){ // need to be boolean
        for(int i = 0; i < expenses.size(); ++i){
            Expense current = expenses.get(i);

            if(current.getId() == expenseId){
                expenses.get(i).setAmount(newAmount);
                expenses.get(i).setCategory(newCategory);
            }
        }
    }

    public void deleteExpense(int expenseId){
        expenses.removeIf( expense ->expense.getId() == expenseId);
    }

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

    public double getTotalSpent(){
        double result = 0.0;
        for(int i = 0; i < expenses.size(); ++i){
            Expense current = expenses.get(i);
            result += expenses.get(i).getAmount();
        }
        return result;

    }

    public List<Expense> getExpenses(){
        return expenses;
    }
}
