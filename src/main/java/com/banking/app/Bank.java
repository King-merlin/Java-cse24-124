package com.banking.model;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class Bank {
    private String name;
    private List<Customer> customers;
    private List<Account> accounts;

    public Bank(String name) {
        this.name = name;
        this.customers = new ArrayList<>();
        this.accounts = new ArrayList<>();
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public Account openAccount(String accountType, Customer customer, double initialDeposit, String branch) {
        return openAccount(accountType, customer, initialDeposit, branch, null, null);
    }

    public Account openAccount(String accountType, Customer customer, double initialDeposit,
                               String branch, String employer, String employerAddress) {
        String accountNumber = generateAccountNumber();
        Account account = null;

        switch (accountType.toLowerCase()) {
            case "savings":
                account = new SavingsAccount(accountNumber, branch, initialDeposit);
                break;
            case "investment":
                account = new InvestmentAccount(accountNumber, branch, initialDeposit);
                break;
            case "cheque":
                if (employer == null || employer.isEmpty()) {
                    throw new IllegalArgumentException("Cheque account requires employer information");
                }
                account = new ChequeAccount(accountNumber, branch, initialDeposit, employer, employerAddress);
                break;
            default:
                throw new IllegalArgumentException("Invalid account type");
        }

        if (account != null) {
            accounts.add(account);
            customer.addAccount(account);
        }

        return account;
    }

    public String generateAccountNumber() {
        return "ACC" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public void calculateMonthlyInterest() {
        for (Account account : accounts) {
            if (account instanceof InterestBearing) {
                ((InterestBearing) account).calculateInterest();
            }
        }
    }

    // Getters
    public String getName() {
        return name;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public List<Account> getAccounts() {
        return accounts;
    }
}
