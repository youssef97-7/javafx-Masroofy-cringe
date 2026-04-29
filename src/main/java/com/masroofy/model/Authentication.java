package com.masroofy.model;

public class Authentication {
    private int pin;
    private boolean locked;

    // added constructor
    public Authentication(int pin)
    {
        this.pin = pin;
        this.locked = false;
    }

    public void setPin(int newPin)
    {
        this.pin = newPin;
    }

    public boolean checkPin(int attemptedPin)
    {

        if (locked)
            return false;
        return this.pin == attemptedPin;
    }

    public void lock()
    {
        this.locked = true;
    }

    public void CloseApplication()
    {
        System.out.println("Closing Application...");
        System.exit(0);
    }
}
