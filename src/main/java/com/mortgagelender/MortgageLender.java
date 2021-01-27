package com.mortgagelender;

public class MortgageLender {
    private double currentAmount;
    public MortgageLender(double amount) {
        this.currentAmount = amount;
    }

    public double checkAvailableFunds() {
        return this.currentAmount;
    }

    public void deposit(double depositedAmount) {
        currentAmount += depositedAmount;
    }
}
