package com.masroofy.controller;

import com.masroofy.model.Authentication;
import com.masroofy.model.BudgetCycle;

import java.time.LocalDate;
/**
 * Controller class responsible for managing application settings.
 * It handles user configuration actions such as updating security credentials
 * and resetting the financial budget cycle.
 *
 * @author Mahmoud Sherif
 * @version 1.0
 */
public class Settings {
    private Authentication auth;

    /**
     * Attempts to change the user's security PIN.
     * Verifies that the newly requested PIN is different from the current one
     * before updating the authentication credentials.
     *
     * @param oldPin The user's current security PIN.
     * @param newPin The desired new security PIN to replace the old one.
     */
    public void changePin(int oldPin, int newPin)
    {
        if(newPin != oldPin)
            auth.setPin(newPin);
        else
            System.out.println("This pin is already your password!");
    }

    /**
     * Resets the application's budget cycle.
     * This initializes a new budget cycle starting from the current date
     * with an allowance of zero.
     */
    public void resetCycle()
    {
        BudgetCycle budgetCycle = new BudgetCycle(LocalDate.now(), LocalDate.now(), 0);
    }
}
