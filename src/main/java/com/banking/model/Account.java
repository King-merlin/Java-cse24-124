package com.banking.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected String branch;
    protected LocalDateTime openDate;
    protected List<Transaction> transactions;

    public Account(String accountNumber, String branch, double initialDeposit) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be empty");
        }
        if (branch == null || branch.trim().isEmpty()) {
            throw new IllegalArgumentException("Branch cannot be empty");
        }
        if (initialDeposit < 0) {
            throw new IllegalArgumentException("Initial deposit cannot be negative");
        }

        this.accountNumber = accountNumber;
        this.branch = branch;
        this.balance = initialDeposit;
        this.openDate = LocalDateTime.now();
        this.transactions = new ArrayList<>();

        if (initialDeposit > 0) {
            transactions.add(new Transaction(initialDeposit, "Initial Deposit"));
        }
    }

    public boolean deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactions.add(new Transaction(amount, "Deposit"));
            return true;
        }
        return false;
    }

    public abstract boolean withdraw(double amount);

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        if (branch != null && !branch.trim().isEmpty()) {
            this.branch = branch;
        }
    }

    public LocalDateTime getOpenDate() {
        return openDate;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public String getAccountType() {
        return this.getClass().getSimpleName().replace("Account", "");
    }
}