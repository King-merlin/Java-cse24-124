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

    // Overloaded openAccount methods
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
        // simple generator: B + 8 digits
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
                    System.out.printf("Applied interest %.2f to %s (new balance: %.2f)%n", interest, a.getAccountNumber(), a.getBalance());
                }
            }
        }
    }
    // add at the bottom of Bank.java

    public void saveToFile(String filename) {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
            for (Customer c : customers.values()) {
                out.printf("CUSTOMER|%s|%s|%s|%s%n", c.getCustomerId(),
                        c.getName(), c.getAddress(), "****");
                for (Account a : c.getAccounts()) {
                    out.printf("ACCOUNT|%s|%s|%.2f|%s%n",
                            c.getCustomerId(),
                            a.getClass().getSimpleName(),
                            a.getBalance(),
                            a.getAccountNumber());
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public static Bank loadFromFile(String filename) {
        Bank bank = new Bank("StudentBank");
        File f = new File(filename);
        if (!f.exists()) return bank;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p[0].equals("CUSTOMER")) {
                    String id = p[1];
                    String[] name = p[2].split(" ");
                    bank.createCustomer(id, name[0], name.length > 1 ? name[1] : "", p[3], "1234");
                } else if (p[0].equals("ACCOUNT")) {
                    String custId = p[1];
                    String type = p[2].replace("Account", "");
                    double bal = Double.parseDouble(p[3]);
                    bank.openAccount(custId, type, bal);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
        return bank;
    }

}
