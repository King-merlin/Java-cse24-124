package banking.controller;

import banking.model.Bank;
import banking.model.Customer;
import banking.model.Account;
import banking.model.SessionManager;
import banking.MainApp;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

public class AdminDashboardController {

    @FXML private TextArea txtArea;

    private Bank bank = Bank.loadFromFile("BankData.txt");

    @FXML
    public void initialize() {
        txtArea.appendText("=== Admin Dashboard ===\n\n");
        txtArea.appendText("Welcome, Admin!\n\n");
        txtArea.appendText("Use the buttons on the right to manage the banking system.\n");
    }

    @FXML
    public void listAllCustomers() {
        txtArea.clear();
        txtArea.appendText("=== All Customers ===\n\n");

        if (bank.getAllCustomers().isEmpty()) {
            txtArea.appendText("No customers in the system.\n");
            return;
        }

        int customerCount = 0;
        for (Customer c : bank.getAllCustomers()) {
            customerCount++;
            txtArea.appendText(String.format("Customer #%d\n", customerCount));
            txtArea.appendText(String.format("ID: %s\n", c.getCustomerId()));
            txtArea.appendText(String.format("Name: %s\n", c.getName()));
            txtArea.appendText(String.format("Address: %s\n", c.getAddress()));
            txtArea.appendText(String.format("Number of Accounts: %d\n", c.getAccounts().size()));

            // Show account details
            if (!c.getAccounts().isEmpty()) {
                txtArea.appendText("Accounts:\n");
                for (Account a : c.getAccounts()) {
                    txtArea.appendText(String.format("  - %s: %s (Balance: $%.2f)\n",
                            a.getAccountNumber(),
                            a.getClass().getSimpleName().replace("Account", ""),
                            a.getBalance()));
                }
            }
            txtArea.appendText("\n");
        }

        txtArea.appendText(String.format("Total Customers: %d\n", customerCount));
    }

    @FXML
    public void applyInterest() {
        txtArea.clear();
        txtArea.appendText("=== Applying Monthly Interest ===\n\n");

        int accountsProcessed = 0;
        double totalInterest = 0.0;

        for (Customer c : bank.getAllCustomers()) {
            for (Account a : c.getAccounts()) {
                double interest = a.calculateMonthlyInterest();
                if (interest > 0) {
                    accountsProcessed++;
                    totalInterest += interest;
                    txtArea.appendText(String.format("Applied $%.2f interest to account %s (%s)\n",
                            interest,
                            a.getAccountNumber(),
                            c.getName()));
                }
            }
        }

        bank.saveToFile("BankData.txt");

        txtArea.appendText(String.format("\n=== Summary ===\n"));
        txtArea.appendText(String.format("Accounts processed: %d\n", accountsProcessed));
        txtArea.appendText(String.format("Total interest applied: $%.2f\n", totalInterest));

        showAlert("Success",
                String.format("Interest applied to %d accounts.\nTotal interest: $%.2f",
                        accountsProcessed, totalInterest));
    }

    @FXML
    public void logout() {
        SessionManager.logout();
        MainApp.loadScreen("/banking/view/WelcomeScreen.fxml");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}