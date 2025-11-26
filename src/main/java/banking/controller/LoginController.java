package banking.controller;

import banking.MainApp;
import banking.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField txtUserId;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblStatus;

    private static final String DATA_FILE = "BankData.txt";
    private Bank bank = Bank.loadFromFile(DATA_FILE);

    @FXML
    public void handleCustomerLogin(ActionEvent e) {
        String id = txtUserId.getText().trim();
        String pw = txtPassword.getText().trim();

        if (id.isEmpty() || pw.isEmpty()) {
            lblStatus.setText("Please enter both ID and password.");
            return;
        }

        Customer c = bank.getCustomer(id);
        if (c != null && c.checkPassword(pw)) {
            lblStatus.setText("Login successful as Customer!");
            SessionManager.setCustomer(c);
            MainApp.loadScreen("/banking/view/CustomerDashboard.fxml");
        } else {
            lblStatus.setText("Invalid customer credentials.");
        }
    }

    @FXML
    public void handleStaffLogin(ActionEvent e) {
        String username = txtUserId.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            lblStatus.setText("Please enter both username and password.");
            return;
        }

        if (username.equals("admin") && password.equals("admin123")) {
            lblStatus.setText("Staff login successful.");
            SessionManager.setAdmin(true);
            MainApp.loadScreen("/banking/view/AdminDashboard.fxml");
        } else {
            lblStatus.setText("Invalid staff credentials.");
        }
    }

    @FXML
    public void handleOpenAccount(ActionEvent e) {
        String id = txtUserId.getText().trim();
        if (id.isEmpty()) {
            lblStatus.setText("Please enter a new customer ID first.");
            return;
        }

        // Check if customer already exists
        if (bank.getCustomer(id) != null) {
            lblStatus.setText("Customer ID already exists. Please use a different ID.");
            return;
        }

        // Create new customer with default values
        Customer newCust = bank.createCustomer(id, "New", "Customer", "Gaborone", "1234");
        bank.openAccount(id, "SAVINGS", 1000);
        bank.saveToFile(DATA_FILE);

        lblStatus.setText("Created new customer with default savings account! Password: 1234");
    }
}