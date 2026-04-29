package com.masroofy.controller;

import com.masroofy.model.Authentication;
import com.masroofy.model.BudgetCycle;

import java.time.LocalDate;

public class Settings {
    private Authentication auth;

    public void changePin(int oldPin, int newPin)
    {
        if(newPin != oldPin)
            auth.setPin(newPin);
        else
            System.out.println("This pin is already your password!");
    }

    public void resetCycle()
    {
        BudgetCycle budgetCycle = new BudgetCycle(LocalDate.now(), LocalDate.now(), 0);
    }
}
