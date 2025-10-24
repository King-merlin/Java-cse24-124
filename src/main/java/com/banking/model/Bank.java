package com.banking.model;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Collections;

public class Bank {
    private String name;
    private List<Customer> customers;
    private List<Account> accounts;

    public Bank(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Bank name cannot be empty");
        }
        this.name = name;
        this.customers = new ArrayList<>();
        this.accounts = new ArrayList<>();
    }

    public void addCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }

        // Check for duplicate customer ID
        for (Customer c : customers) {
            if (c.getCustomerId().equals(customer.getCustomerId())) {
                throw new IllegalArgumentException("Customer ID already exists");
            }
        }

        customers.add(customer);
    }

    public Account openAccount(String accountType, Customer customer, double initialDeposit, String branch) {
        return openAccount(accountType, customer, initialDeposit, branch, null, null);
    }

    public Account openAccount(String accountType, Customer customer, double initialDeposit,
                               String branch, String employer, String employerAddress) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        if (accountType == null || accountType.trim().isEmpty()) {
            throw new IllegalArgumentException("Account type cannot be empty");
        }

        String accountNumber = generateAccountNumber();
        Account account = null;

        switch (accountType.toLowerCase().trim()) {
            case "savings":
                account = new SavingsAccount(accountNumber, branch, initialDeposit);
                break;
            case "investment":
                account = new InvestmentAccount(accountNumber, branch, initialDeposit);
                break;
            case "cheque":
                if (employer == null || employer.trim().isEmpty()) {
                    throw new IllegalArgumentException("Cheque account requires employer information");
                }
                if (!customer.canOpenChequeAccount()) {
                    throw new IllegalArgumentException("Customer is not eligible for a cheque account");
                }
                account = new ChequeAccount(accountNumber, branch, initialDeposit, employer, employerAddress);
                break;
            default:
                throw new IllegalArgumentException("Invalid account type: " + accountType);
        }

        accounts.add(account);
        customer.addAccount(account);

        return account;
    }

    public String generateAccountNumber() {
        return "ACC" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public void calculateMonthlyInterest() {
        int count = 0;
        for (Account account : accounts) {
            if (account instanceof InterestBearing) {
                ((InterestBearing) account).calculateInterest();
                count++;
            }
        }
        System.out.println("Interest calculated for " + count + " accounts");
    }

    public Customer findCustomer(String customerId) {
        if (customerId == null) return null;

        for (Customer customer : customers) {
            if (customer.getCustomerId().equals(customerId)) {
                return customer;
            }
        }
        return null;
    }

    public Account findAccount(String accountNumber) {
        if (accountNumber == null) return null;

        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public List<Customer> getCustomers() {
        return Collections.unmodifiableList(customers);
    }

    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }
}
