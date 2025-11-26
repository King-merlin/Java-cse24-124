package banking.controller;

import banking.model.Bank;
import banking.model.Customer;
import banking.model.Account;
import banking.model.SessionManager;
import banking.model.Admin;
import banking.MainApp;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboardController {

    @FXML private TextArea txtArea;

    private Bank bank;
    private static List<Admin> adminList = new ArrayList<>();

    // Initialize with default admin
    static {
        adminList.add(new Admin("admin", "admin123", "System Administrator", LocalDate.now().toString()));
    }

    @FXML
    public void initialize() {
        // Load the bank data in initialize() method instead
        bank = Bank.loadFromFile("BankData.txt");

        txtArea.appendText("=== Admin Dashboard ===\n\n");
        txtArea.appendText("Welcome, Admin!\n\n");
        txtArea.appendText("Use the buttons on the right to manage the banking system.\n");
    }

    @FXML
    public void listAllCustomers() {
        // Reload to get latest data
        bank = Bank.loadFromFile("BankData.txt");

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
        // Reload to get latest data
        bank = Bank.loadFromFile("BankData.txt");

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
    public void createAdminAccount() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create Admin Account");
        dialog.setHeaderText("Enter new administrator information");

        ButtonType createButtonType = new ButtonType("Create Admin", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField usernameField = new TextField();
        usernameField.setPromptText("e.g., admin2");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm password");

        TextField fullNameField = new TextField();
        fullNameField.setPromptText("e.g., John Doe");

        Label infoLabel = new Label(
                "⚠ Important:\n" +
                        "• Username must be unique\n" +
                        "• Password must be at least 6 characters\n" +
                        "• Admin accounts have full system access"
        );
        infoLabel.setStyle("-fx-background-color: #fff3cd; -fx-padding: 10; " +
                "-fx-border-color: #ffc107; -fx-border-radius: 5; " +
                "-fx-background-radius: 5; -fx-font-size: 11px;");
        infoLabel.setWrapText(true);
        infoLabel.setMaxWidth(300);

        int row = 0;
        grid.add(new Label("Username:"), 0, row);
        grid.add(usernameField, 1, row++);

        grid.add(new Label("Password:"), 0, row);
        grid.add(passwordField, 1, row++);

        grid.add(new Label("Confirm Password:"), 0, row);
        grid.add(confirmPasswordField, 1, row++);

        grid.add(new Label("Full Name:"), 0, row);
        grid.add(fullNameField, 1, row++);

        grid.add(infoLabel, 0, row, 2, 1);

        dialog.getDialogPane().setContent(grid);
        usernameField.requestFocus();

        dialog.showAndWait().ifPresent(response -> {
            if (response == createButtonType) {
                try {
                    String username = usernameField.getText().trim();
                    String password = passwordField.getText().trim();
                    String confirmPassword = confirmPasswordField.getText().trim();
                    String fullName = fullNameField.getText().trim();

                    // Validation
                    if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
                        showAlert("Error", "All fields are required.");
                        return;
                    }

                    if (password.length() < 6) {
                        showAlert("Error", "Password must be at least 6 characters long.");
                        return;
                    }

                    if (!password.equals(confirmPassword)) {
                        showAlert("Error", "Passwords do not match.");
                        return;
                    }

                    // Check if username already exists
                    for (Admin admin : adminList) {
                        if (admin.getUsername().equalsIgnoreCase(username)) {
                            showAlert("Error", "Username already exists. Please choose a different username.");
                            return;
                        }
                    }

                    // Create new admin
                    Admin newAdmin = new Admin(username, password, fullName, LocalDate.now().toString());
                    adminList.add(newAdmin);

                    txtArea.clear();
                    txtArea.appendText("=== Admin Account Created Successfully ===\n\n");
                    txtArea.appendText(String.format("Username: %s\n", username));
                    txtArea.appendText(String.format("Full Name: %s\n", fullName));
                    txtArea.appendText(String.format("Created Date: %s\n\n", newAdmin.getCreatedDate()));
                    txtArea.appendText("The new admin can now login with these credentials.\n\n");
                    txtArea.appendText("=== All Admin Accounts ===\n");

                    int count = 0;
                    for (Admin admin : adminList) {
                        count++;
                        txtArea.appendText(String.format("%d. %s (%s) - Created: %s\n",
                                count, admin.getUsername(), admin.getFullName(), admin.getCreatedDate()));
                    }

                    showAlert("Success",
                            String.format("Admin account created successfully!\n\n" +
                                            "Username: %s\n" +
                                            "Full Name: %s\n\n" +
                                            "The admin can now login with their credentials.",
                                    username, fullName));

                } catch (Exception e) {
                    showAlert("Error", "Failed to create admin account: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
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

    // Static method to validate admin credentials (can be called from login controllers)
    public static boolean validateAdmin(String username, String password) {
        for (Admin admin : adminList) {
            if (admin.getUsername().equals(username) && admin.checkPassword(password)) {
                return true;
            }
        }
        return false;
    }

    // Static method to get all admins
    public static List<Admin> getAllAdmins() {
        return new ArrayList<>(adminList);
    }
}