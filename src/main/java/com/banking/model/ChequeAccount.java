package com.banking.model;

public class ChequeAccount extends Account {
    private String employer;
    private String employerAddress;

    public ChequeAccount(String accountNumber, String branch, double initialDeposit,
                         String employer, String employerAddress) {
        super(accountNumber, branch, initialDeposit);

        if (employer == null || employer.trim().isEmpty()) {
            throw new IllegalArgumentException("Employer information is required");
        }
        if (employerAddress == null || employerAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Employer address is required");
        }

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

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        if (employer != null && !employer.trim().isEmpty()) {
            this.employer = employer;
        }
    }

    public String getEmployerAddress() {
        return employerAddress;
    }

    public void setEmployerAddress(String employerAddress) {
        if (employerAddress != null && !employerAddress.trim().isEmpty()) {
            this.employerAddress = employerAddress;
        }
    }
}