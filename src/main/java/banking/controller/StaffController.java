package banking.controller;

import banking.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class StaffController {
    @FXML private TextArea txtArea;

    private Bank bank;
    private static final String DATA_FILE = "BankData.txt";

    public void setBank(Bank b) {
        this.bank = b;
        txtArea.appendText("=== Staff Dashboard ===\n\n");
        txtArea.appendText("Staff logged in successfully.\n");
        txtArea.appendText("Use the buttons to manage customers and accounts.\n");
    }

    @FXML
    public void listCustomers() {
        txtArea.clear();
        txtArea.appendText("=== All Customers ===\n\n");

        if (bank.getAllCustomers().isEmpty()) {
            txtArea.appendText("No customers in the system.\n");
            return;
        }

        int count = 0;
        for (Customer c : bank.getAllCustomers()) {
            count++;
            txtArea.appendText(String.format("%d. %s (ID: %s)\n", count, c.getName(), c.getCustomerId()));
            txtArea.appendText(String.format("   Address: %s\n", c.getAddress()));
            txtArea.appendText(String.format("   Accounts: %d\n", c.getAccounts().size()));

            for (Account a : c.getAccounts()) {
                txtArea.appendText(String.format("     - %s: %s ($%.2f)\n",
                        a.getAccountNumber(),
                        a.getClass().getSimpleName().replace("Account", ""),
                        a.getBalance()));
            }
            txtArea.appendText("\n");
        }

        txtArea.appendText(String.format("Total: %d customers\n", count));
    }

    @FXML
    public void calculateInterest() {
        txtArea.clear();
        txtArea.appendText("=== Calculating Interest ===\n\n");

        int processed = 0;
        double total = 0.0;

        for (Customer c : bank.getAllCustomers()) {
            for (Account a : c.getAccounts()) {
                double interest = a.calculateMonthlyInterest();
                if (interest > 0) {
                    processed++;
                    total += interest;
                    txtArea.appendText(String.format("$%.2f interest → %s (%s)\n",
                            interest, a.getAccountNumber(), c.getName()));
                }
            }
        }

        bank.saveToFile(DATA_FILE);

        txtArea.appendText(String.format("\nProcessed %d accounts\n", processed));
        txtArea.appendText(String.format("Total interest: $%.2f\n", total));

        showAlert("Interest Applied",
                String.format("Applied interest to %d accounts.\nTotal: $%.2f", processed, total));
    }

    @FXML
    public void saveAndExit() {
        bank.saveToFile(DATA_FILE);
        Stage stage = (Stage) txtArea.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}