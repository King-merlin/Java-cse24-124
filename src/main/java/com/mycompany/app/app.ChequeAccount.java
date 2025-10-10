package com.mycompany.app;

public class ChequeAccount extends Account {
    private String employer;
    private String employerAddress;

    public ChequeAccount(String accountNumber, String branch, double initialDeposit,
                         String employer, String employerAddress) {
        super(accountNumber, branch, initialDeposit);
        this.employer = employer;
        this.employerAddress = employerAddress;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            transactions.add(new Transaction(amount, "Withdrawal"));
            return true;
        }
        return false;
    }

    // Getters and Setters
    public String getEmployer() { 
        return employer;
    }
    public void setEmployer(String employer) {
        this.employer = employer;
    }
    public String getEmployerAddress() { 
        return employerAddress; 
    }
    public void setEmployerAddress(String employerAddress) {
        this.employerAddress = employerAddress;
    }
}
