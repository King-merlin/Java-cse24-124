package banking.controller;

import banking.MainApp;
import banking.model.Bank;
import banking.model.Customer;
import banking.model.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

public class WelcomeController {

    private static final String DATA_FILE = "BankData.txt";
    private Bank bank = Bank.loadFromFile(DATA_FILE);

    @FXML
    public void handleCustomerLogin() {
        // Get customer ID
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Customer Login");
        idDialog.setHeaderText("Enter your Customer ID:");
        String customerId = idDialog.showAndWait().orElse("");

        if (customerId.isEmpty()) return;

        // Get password
        TextInputDialog pwDialog = new TextInputDialog();
        pwDialog.setTitle("Customer Login");
        pwDialog.setHeaderText("Enter your Password:");
        String password = pwDialog.showAndWait().orElse("");

        Customer customer = bank.getCustomer(customerId);
        if (customer != null && customer.checkPassword(password)) {
            SessionManager.setCustomer(customer);
            MainApp.loadScreen("/banking/view/CustomerDashboard.fxml");
        } else {
            showAlert("Login Failed", "Invalid customer ID or password.");
        }
    }

    @FXML
    public void handleAdminLogin() {
        // Get admin username
        TextInputDialog userDialog = new TextInputDialog();
        userDialog.setTitle("Admin Login");
        userDialog.setHeaderText("Enter admin username:");
        String username = userDialog.showAndWait().orElse("");

        if (username.isEmpty()) return;

        // Get password
        TextInputDialog pwDialog = new TextInputDialog();
        pwDialog.setTitle("Admin Login");
        pwDialog.setHeaderText("Enter admin password:");
        String password = pwDialog.showAndWait().orElse("");

        if (username.equals("admin") && password.equals("admin123")) {
            SessionManager.setAdmin(true);
            MainApp.loadScreen("/banking/view/AdminDashboard.fxml");
        } else {
            showAlert("Login Failed", "Invalid admin credentials.");
        }
    }

    @FXML
    public void handleOpenAccount() {
        // Get new customer ID
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Open Account");
        idDialog.setHeaderText("Enter new Customer ID:");
        String customerId = idDialog.showAndWait().orElse("");

        if (customerId.isEmpty()) {
            showAlert("Error", "Customer ID is required.");
            return;
        }

        // Check if customer already exists
        if (bank.getCustomer(customerId) != null) {
            showAlert("Error", "Customer ID already exists.");
            return;
        }

        // Get first name
        TextInputDialog fnDialog = new TextInputDialog();
        fnDialog.setTitle("Open Account");
        fnDialog.setHeaderText("Enter First Name:");
        String firstName = fnDialog.showAndWait().orElse("");

        // Get last name
        TextInputDialog lnDialog = new TextInputDialog();
        lnDialog.setTitle("Open Account");
        lnDialog.setHeaderText("Enter Last Name:");
        String lastName = lnDialog.showAndWait().orElse("");

        // Get address
        TextInputDialog addrDialog = new TextInputDialog();
        addrDialog.setTitle("Open Account");
        addrDialog.setHeaderText("Enter Address:");
        String address = addrDialog.showAndWait().orElse("Gaborone");

        // Get password
        TextInputDialog pwDialog = new TextInputDialog();
        pwDialog.setTitle("Open Account");
        pwDialog.setHeaderText("Create Password:");
        String password = pwDialog.showAndWait().orElse("1234");

        // Get initial deposit
        TextInputDialog depositDialog = new TextInputDialog("1000");
        depositDialog.setTitle("Open Account");
        depositDialog.setHeaderText("Enter Initial Deposit:");
        double initialDeposit = Double.parseDouble(depositDialog.showAndWait().orElse("1000"));

        // Create customer and account
        Customer newCustomer = bank.createCustomer(customerId, firstName, lastName, address, password);
        bank.openAccount(customerId, "SAVINGS", initialDeposit);
        bank.saveToFile(DATA_FILE);

        showAlert("Success", "Account created successfully!\nCustomer ID: " + customerId + "\nPassword: " + password + "\nInitial Deposit: " + initialDeposit);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}