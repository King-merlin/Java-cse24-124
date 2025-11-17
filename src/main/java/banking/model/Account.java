package banking.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    private String accountNumber;
    private double balance;
    private String branch;
    private String openDate;
    protected List<Transaction> transactions = new ArrayList<>();

    public Account(String accountNumber, double initialDeposit, String branch, String openDate) {
        this.accountNumber = accountNumber;
        this.balance = initialDeposit;
        this.branch = branch;
        this.openDate = openDate;
    }

    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public String getBranch() { return branch; }
    public String getOpenDate() { return openDate; }

    protected void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Deposit must be > 0");
            return;
        }
        balance += amount;
        addTransaction(new Transaction("DEPOSIT", amount));
        System.out.printf("Deposited %.2f to %s. New balance: %.2f%n", amount, accountNumber, balance);
    }

    /**
     * Attempt a withdrawal. Concrete accounts will decide if allowed.
     * @param amount amount to withdraw
     * @return true if successful
     */
    public abstract boolean withdraw(double amount);

    public abstract double calculateMonthlyInterest();

    protected void changeBalance(double delta) {
        this.balance += delta;
    }

    @Override
    public String toString() {
        return String.format("%s - %s: %.2f (Branch: %s)", this.getClass().getSimpleName(), accountNumber, balance, branch);
    }
}
