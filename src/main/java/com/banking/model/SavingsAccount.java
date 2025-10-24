package com.banking.model;

public class SavingsAccount extends Account implements InterestBearing {
    private static final double INTEREST_RATE = 0.0005; // 0.05% monthly (0.6% annual)

    public SavingsAccount(String accountNumber, String branch, double initialDeposit) {
        super(accountNumber, branch, initialDeposit);
    }

    @Override
    public boolean withdraw(double amount) {
        // Savings accounts have restrictions on withdrawals
        // For now, no withdrawals allowed - can be modified based on requirements
        return false;
    }

    @Override
    public void calculateInterest() {
        if (balance > 0) {
            double interest = balance * INTEREST_RATE;
            balance += interest;
            transactions.add(new Transaction(interest, "Interest Payment"));
        }
    }

    @Override
    public double getInterestRate() {
        return INTEREST_RATE;
    }

    public double getAnnualInterestRate() {
        return INTEREST_RATE * 12 * 100; // As percentage
    }
}