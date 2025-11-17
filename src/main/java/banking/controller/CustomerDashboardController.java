package banking.controller;

import banking.MainApp;
import banking.model.Bank;         // <-- add this
import banking.model.Customer;     // <-- and any other model classes you use
import banking.model.Account;
import banking.model.SessionManager;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;

public class CustomerDashboardController {

    @FXML private TextArea txtArea;
    private Bank bank = Bank.loadFromFile("BankData.txt");
    private Customer customer;

    @FXML
    public void initialize() {
        customer = SessionManager.getCustomer();
        txtArea.appendText("Welcome, " + customer.getName() + "!\n");
        listAccounts();
    }

    @FXML
    public void listAccounts() {
        txtArea.clear();
        for (Account a : customer.getAccounts()) {
            txtArea.appendText(a + "\n");
        }
    }

    @FXML
    public void deposit() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Enter account number:");
        String accNum = dialog.showAndWait().orElse("");
        Account a = findAccount(accNum);
        if (a == null) {
            txtArea.appendText("No account found.\n");
            return;
        }

        TextInputDialog amtDialog = new TextInputDialog();
        amtDialog.setHeaderText("Enter deposit amount:");
        double amt = Double.parseDouble(amtDialog.showAndWait().orElse("0"));
        a.deposit(amt);
        txtArea.appendText("Deposited " + amt + " to " + accNum + "\n");
        bank.saveToFile("BankData.txt");
    }

    @FXML
    public void withdraw() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Enter account number:");
        String accNum = dialog.showAndWait().orElse("");
        Account a = findAccount(accNum);
        if (a == null) {
            txtArea.appendText("No account found.\n");
            return;
        }

        TextInputDialog amtDialog = new TextInputDialog();
        amtDialog.setHeaderText("Enter withdrawal amount:");
        double amt = Double.parseDouble(amtDialog.showAndWait().orElse("0"));
        a.withdraw(amt);
        txtArea.appendText("Attempted withdrawal of " + amt + " from " + accNum + "\n");
        bank.saveToFile("BankData.txt");
    }

    private Account findAccount(String accNum) {
        for (Account a : customer.getAccounts()) {
            if (a.getAccountNumber().equals(accNum)) return a;
        }
        return null;
    }

    @FXML
    public void logout() {
        SessionManager.logout();
        MainApp.loadScreen("/banking/view/WelcomeScreen.fxml");
    }
}
