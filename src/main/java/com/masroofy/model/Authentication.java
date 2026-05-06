package com.masroofy.model;

/**
 * Model class representing the authentication system for the application.
 * It manages the user's security PIN, handles verification, and controls
 * the locked state to prevent unauthorized access.
 *
 * @author Mahmoud Sherif
 * @version 1.0
 */
public class Authentication {
    private int pin;
    private boolean locked;

    /**
     * Constructs a new Authentication instance with the specified initial PIN.
     * The system is unlocked by default upon initialization.
     *
     * @param pin The initial security PIN to be set for the user.
     */
    public Authentication(int pin)
    {
        this.pin = pin;
        this.locked = false;
    }

    /**
     * Updates the user's security PIN to a new value.
     *
     * @param newPin The new security PIN to replace the existing one.
     */
    public void setPin(int newPin)
    {
        this.pin = newPin;
    }

    /**
     * Verifies if the provided PIN matches the stored security PIN.
     * If the authentication system is currently locked, this method will
     * automatically return false regardless of the provided PIN.
     *
     * @param attemptedPin The PIN entered by the user attempting to authenticate.
     * @return true if the system is unlocked and the PIN matches; false otherwise.
     */
    public boolean checkPin(int attemptedPin)
    {

        if (locked)
            return false;
        return this.pin == attemptedPin;
    }

    /**
     * Locks the authentication system. Once locked, all subsequent PIN checks
     * will automatically fail.
     */
    public void lock()
    {
        this.locked = true;
    }

    /**
     * Forcibly terminates the application and exits the Java Virtual Machine.
     */
    public void CloseApplication()
    {
        System.out.println("Closing Application...");
        System.exit(0);
    }
}
