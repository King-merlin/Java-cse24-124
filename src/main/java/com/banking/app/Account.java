package com.banking.model;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected String branch;
    protected Date openDate;
    protected List<Transaction> transactions;

    public Account(String accountNumber, String branch, double initialDeposit) {
        this.accountNumber = accountNumber;
        this.branch = branch;
        this.balance = initialDeposit;
        this.openDate = new Date();
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

    // Getters (removed dangerous setBalance)
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}