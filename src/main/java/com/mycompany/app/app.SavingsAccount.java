package com.mycompany.app;

public class SavingsAccount extends Account implements InterestBearing {
    private static final double INTEREST_RATE = 0.0005; // 0.05% monthly

    public SavingsAccount(String accountNumber, String branch, double initialDeposit) {
        super(accountNumber, branch, initialDeposit);
    }

    @Override
    public boolean withdraw(double amount) {
            //no withdrawals
        return false;
    }

    @Override
    public void calculateInterest() {
        double interest = balance * INTEREST_RATE;
        balance += interest;
        transactions.add(new Transaction(interest, "Interest Payment"));
    }

    @Override
    public double getInterestRate() {
        return INTEREST_RATE;
    }
}
