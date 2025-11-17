package banking.model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String customerId;
    private String firstName;
    private String lastName;
    private String address;
    private String password;
    private List<Account> accounts = new ArrayList<>();

    public Customer(String customerId, String firstName, String lastName, String address, String password) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.password = password;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public boolean checkPassword(String pw) {
        return password.equals(pw);
    }

    public void addAccount(Account a) {
        accounts.add(a);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public boolean removeAccount(String accountNumber) {
        return accounts.removeIf(a -> a.getAccountNumber().equals(accountNumber));
    }

    @Override
    public String toString() {
        return customerId + " - " + getName();
    }
}