package banking.model;

import java.io.FileWriter;
import java.time.LocalDate;
import java.io.*;
import java.util.*;

public class Bank {
    private String name;
    private Map<String, Customer> customers = new HashMap<>();
    private Set<String> accountNumbers = new HashSet<>();
    private Random random = new Random();

    public Bank(String name) {
        this.name = name;
    }

    public Customer createCustomer(String id, String fn, String ln, String address, String password) {
        Customer c = new Customer(id, fn, ln, address, password);
        customers.put(id, c);
        return c;
    }

    public Customer getCustomer(String id) {
        return customers.get(id);
    }

    public Account openAccount(String customerId, String accountType, double initialDeposit) {
        return openAccount(customerId, accountType, initialDeposit, null, null);
    }

    public Account openAccount(String customerId, String accountType, double initialDeposit, String employer, String employerAddress) {
        Customer c = customers.get(customerId);
        if (c == null) {
            System.out.println("Customer not found.");
            return null;
        }
        String accNum = generateAccountNumber();
        String branch = "Main";
        String openDate = LocalDate.now().toString();

        Account account = null;
        switch (accountType.toUpperCase()) {
            case "SAVINGS":
                account = new SavingsAccount(accNum, initialDeposit, branch, openDate);
                break;
            case "INVESTMENT":
                if (initialDeposit < 500) {
                    System.out.println("Investment accounts require an initial deposit of at least 500.00");
                    return null;
                }
                account = new InvestmentAccount(accNum, initialDeposit, branch, openDate);
                break;
            case "CHEQUE":
                if (employer == null || employer.isEmpty()) {
                    System.out.println("Cheque accounts require employer information.");
                    return null;
                }
                account = new ChequeAccount(accNum, initialDeposit, branch, openDate, employer, employerAddress);
                break;
            default:
                System.out.println("Unknown account type.");
                return null;
        }

        accountNumbers.add(accNum);
        c.addAccount(account);
        System.out.printf("%s opened for %s with account number %s and initial deposit %.2f%n",
                accountType, c.getName(), accNum, initialDeposit);
        return account;
    }

    private String generateAccountNumber() {
        String num;
        do {
            num = "B" + (10000000 + random.nextInt(90000000));
        } while (accountNumbers.contains(num));
        return num;
    }

    public Collection<Customer> getAllCustomers() {
        return customers.values();
    }

    public boolean deleteAccount(String customerId, String accountNumber) {
        Customer c = customers.get(customerId);
        if (c == null) return false;
        boolean removed = c.removeAccount(accountNumber);
        if (removed) {
            accountNumbers.remove(accountNumber);
        }
        return removed;
    }

    public void calculateInterestForAllCustomers() {
        for (Customer c : customers.values()) {
            for (Account a : c.getAccounts()) {
                if (a instanceof InterestBearing) {
                    double interest = ((InterestBearing)a).calculateMonthlyInterest();
                    System.out.printf("Applied interest %.2f to %s (new balance: %.2f)%n",
                            interest, a.getAccountNumber(), a.getBalance());
                }
            }
        }
    }

    /**
     * Save bank data to file
     * Format: CUSTOMER|customerId|firstName|lastName|address|password
     *         ACCOUNT|customerId|accountType|balance|accountNumber|branch|openDate|employer|employerAddress
     */
    public void saveToFile(String filename) {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
            for (Customer c : customers.values()) {
                out.printf("CUSTOMER|%s|%s|%s|%s|%s%n",
                        c.getCustomerId(),
                        c.getName().split(" ")[0],
                        c.getName().split(" ").length > 1 ? c.getName().split(" ")[1] : "",
                        c.getAddress(),
                        c.getPassword());

                for (Account a : c.getAccounts()) {
                    if (a instanceof ChequeAccount) {
                        ChequeAccount ca = (ChequeAccount) a;
                        out.printf("ACCOUNT|%s|%s|%.2f|%s|%s|%s|%s|%s%n",
                                c.getCustomerId(),
                                a.getClass().getSimpleName(),
                                a.getBalance(),
                                a.getAccountNumber(),
                                a.getBranch(),
                                a.getOpenDate(),
                                ca.getEmployer(),
                                ca.getEmployerAddress());
                    } else {
                        out.printf("ACCOUNT|%s|%s|%.2f|%s|%s|%s%n",
                                c.getCustomerId(),
                                a.getClass().getSimpleName(),
                                a.getBalance(),
                                a.getAccountNumber(),
                                a.getBranch(),
                                a.getOpenDate());
                    }
                }
            }
            System.out.println("Data saved successfully to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    /**
     * Load bank data from file
     * Format: CUSTOMER|customerId|firstName|lastName|address|password
     *         ACCOUNT|customerId|accountType|balance|accountNumber|branch|openDate|employer|employerAddress
     */
    public static Bank loadFromFile(String filename) {
        Bank bank = new Bank("StudentBank");
        File f = new File(filename);

        if (!f.exists()) {
            System.out.println("Data file not found. Starting with empty bank.");
            return bank;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");

                if (parts[0].equals("CUSTOMER")) {
                    String customerId = parts[1];
                    String firstName = parts[2];
                    String lastName = parts.length > 3 ? parts[3] : "";
                    String address = parts.length > 4 ? parts[4] : "Gaborone";
                    String password = parts.length > 5 ? parts[5] : "1234";

                    bank.createCustomer(customerId, firstName, lastName, address, password);
                    System.out.println("Loaded customer: " + customerId);

                } else if (parts[0].equals("ACCOUNT")) {
                    String customerId = parts[1];
                    String accountType = parts[2].replace("Account", "");
                    double balance = Double.parseDouble(parts[3]);
                    String accountNumber = parts.length > 4 ? parts[4] : null;
                    String branch = parts.length > 5 ? parts[5] : "Main";
                    String openDate = parts.length > 6 ? parts[6] : LocalDate.now().toString();
                    String employer = parts.length > 7 ? parts[7] : "Unknown";
                    String employerAddress = parts.length > 8 ? parts[8] : "Unknown";

                    Customer customer = bank.getCustomer(customerId);
                    if (customer != null) {
                        Account account = null;
                        switch (accountType.toUpperCase()) {
                            case "SAVINGS":
                                account = new SavingsAccount(accountNumber, balance, branch, openDate);
                                break;
                            case "INVESTMENT":
                                account = new InvestmentAccount(accountNumber, balance, branch, openDate);
                                break;
                            case "CHEQUE":
                                account = new ChequeAccount(accountNumber, balance, branch, openDate, employer, employerAddress);
                                break;
                        }

                        if (account != null) {
                            customer.addAccount(account);
                            bank.accountNumbers.add(accountNumber);
                            System.out.println("Loaded account: " + accountNumber + " for customer: " + customerId);
                        }
                    }
                }
            }
            System.out.println("Data loaded successfully from " + filename);
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error parsing data: " + e.getMessage());
            e.printStackTrace();
        }

        return bank;
    }
}