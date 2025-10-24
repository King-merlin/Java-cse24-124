package com.banking.model;

public class InvestmentAccount extends Account implements InterestBearing {
    private static final double INTEREST_RATE = 0.004167; // ~5% annual (0.4167% monthly)
    private static final double MINIMUM_BALANCE = 500.00;

    public InvestmentAccount(String accountNumber, String branch, double initialDeposit) {
        super(accountNumber, branch, initialDeposit);
        if (initialDeposit < MINIMUM_BALANCE) {
            throw new IllegalArgumentException(
                    String.format("Investment account requires minimum BWP %.2f", MINIMUM_BALANCE)
            );
        }
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance && (balance - amount) >= MINIMUM_BALANCE) {
            balance -= amount;
            transactions.add(new Transaction(amount, "Withdrawal"));
            return true;
        }
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

    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }

    public double getAnnualInterestRate() {
        return INTEREST_RATE * 12 * 100; // As percentage
    }
}