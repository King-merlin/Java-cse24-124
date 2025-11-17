package banking.controller;

import banking.model.Bank;     // <-- add this
import banking.model.Customer;
import banking.model.SessionManager;
import banking.MainApp;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class AdminDashboardController {

    @FXML private TextArea txtArea;

    private Bank bank = Bank.loadFromFile("BankData.txt");

    @FXML
    public void initialize() {
        txtArea.appendText("Welcome, Admin!\n");
    }

    @FXML
    public void listAllCustomers() {
        txtArea.clear();
        for (Customer c : bank.getAllCustomers()) {
            txtArea.appendText(c + " - " + c.getAccounts().size() + " accounts\n");
        }
    }

    @FXML
    public void applyInterest() {
        bank.calculateInterestForAllCustomers();
        txtArea.appendText("Applied monthly interest to eligible accounts.\n");
        bank.saveToFile("BankData.txt");
    }

    @FXML
    public void logout() {
        SessionManager.logout();
        MainApp.loadScreen("/banking/view/WelcomeScreen.fxml");
    }
}
