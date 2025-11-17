package banking.controller;

import banking.MainApp;
import banking.model.Bank;
import banking.model.Customer;
import banking.model.SessionManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class WelcomeController {

    private static final String DATA_FILE = "BankData.txt";
    private Bank bank = Bank.loadFromFile(DATA_FILE);

    @FXML
    public void handleCustomerLogin() {
        // Create custom dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Customer Login");
        dialog.setHeaderText("Enter your credentials");

        // Set button types
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField customerIdField = new TextField();
        customerIdField.setPromptText("Customer ID");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        grid.add(new Label("Customer ID:"), 0, 0);
        grid.add(customerIdField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Request focus on customer ID field
        customerIdField.requestFocus();

        // Show dialog and process result
        dialog.showAndWait().ifPresent(response -> {
            if (response == loginButtonType) {
                String customerId = customerIdField.getText().trim();
                String password = passwordField.getText().trim();

                if (customerId.isEmpty() || password.isEmpty()) {
                    showAlert("Error", "Please fill in all fields.");
                    return;
                }

                Customer customer = bank.getCustomer(customerId);
                if (customer != null && customer.checkPassword(password)) {
                    SessionManager.setCustomer(customer);
                    MainApp.loadScreen("/banking/view/CustomerDashboard.fxml");
                } else {
                    showAlert("Login Failed", "Invalid customer ID or password.");
                }
            }
        });
    }

    @FXML
    public void handleAdminLogin() {
        // Create custom dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Admin Login");
        dialog.setHeaderText("Enter admin credentials");

        // Set button types
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Request focus on username field
        usernameField.requestFocus();

        // Show dialog and process result
        dialog.showAndWait().ifPresent(response -> {
            if (response == loginButtonType) {
                String username = usernameField.getText().trim();
                String password = passwordField.getText().trim();

                if (username.equals("admin") && password.equals("admin123")) {
                    SessionManager.setAdmin(true);
                    MainApp.loadScreen("/banking/view/AdminDashboard.fxml");
                } else {
                    showAlert("Login Failed", "Invalid admin credentials.");
                }
            }
        });
    }

    @FXML
    public void handleOpenAccount() {
        // Create custom dialog with all fields
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Open New Account");
        dialog.setHeaderText("Enter customer information");

        // Set button types
        ButtonType createButtonType = new ButtonType("Create Account", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        // Create the form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField customerIdField = new TextField();
        customerIdField.setPromptText("e.g., CUST001");
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First name");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last name");
        TextField addressField = new TextField();
        addressField.setPromptText("Address");
        addressField.setText("Gaborone"); // Default value
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Create password");

        // Account type selection
        ComboBox<String> accountTypeCombo = new ComboBox<>();
        accountTypeCombo.getItems().addAll("SAVINGS", "CHEQUE", "INVESTMENT");
        accountTypeCombo.setValue("SAVINGS"); // Default

        TextField depositField = new TextField();
        depositField.setPromptText("Initial deposit amount");
        depositField.setText("1000"); // Default value

        Label infoLabel = new Label(
                "Account Types:\n" +
                        "• Savings: No withdrawals, 0.05% monthly interest\n" +
                        "• Cheque: Normal withdrawals, no interest\n" +
                        "• Investment: Min $500 balance, 5% monthly interest"
        );
        infoLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #666;");
        infoLabel.setWrapText(true);

        // Add all fields to grid
        int row = 0;
        grid.add(new Label("Customer ID:"), 0, row);
        grid.add(customerIdField, 1, row++);

        grid.add(new Label("First Name:"), 0, row);
        grid.add(firstNameField, 1, row++);

        grid.add(new Label("Last Name:"), 0, row);
        grid.add(lastNameField, 1, row++);

        grid.add(new Label("Address:"), 0, row);
        grid.add(addressField, 1, row++);

        grid.add(new Label("Password:"), 0, row);
        grid.add(passwordField, 1, row++);

        grid.add(new Label("Account Type:"), 0, row);
        grid.add(accountTypeCombo, 1, row++);

        grid.add(new Label("Initial Deposit:"), 0, row);
        grid.add(depositField, 1, row++);

        grid.add(infoLabel, 0, row, 2, 1);

        dialog.getDialogPane().setContent(grid);

        // Request focus on customer ID field
        customerIdField.requestFocus();

        // Show dialog and process result
        dialog.showAndWait().ifPresent(response -> {
            if (response == createButtonType) {
                try {
                    String customerId = customerIdField.getText().trim();
                    String firstName = firstNameField.getText().trim();
                    String lastName = lastNameField.getText().trim();
                    String address = addressField.getText().trim();
                    String password = passwordField.getText().trim();
                    String accountType = accountTypeCombo.getValue();
                    String depositText = depositField.getText().trim();

                    // Validation
                    if (customerId.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
                            address.isEmpty() || password.isEmpty() || depositText.isEmpty()) {
                        showAlert("Error", "Please fill in all fields.");
                        return;
                    }

                    // Check if customer already exists
                    if (bank.getCustomer(customerId) != null) {
                        showAlert("Error", "Customer ID already exists. Please choose a different ID.");
                        return;
                    }

                    double initialDeposit;
                    try {
                        initialDeposit = Double.parseDouble(depositText);
                        if (initialDeposit < 0) {
                            showAlert("Error", "Initial deposit must be a positive number.");
                            return;
                        }

                        // Check minimum for investment accounts
                        if (accountType.equals("INVESTMENT") && initialDeposit < 500) {
                            showAlert("Error", "Investment accounts require a minimum initial deposit of $500.");
                            return;
                        }
                    } catch (NumberFormatException e) {
                        showAlert("Error", "Invalid deposit amount. Please enter a valid number.");
                        return;
                    }

                    // Create customer and account
                    Customer newCustomer = bank.createCustomer(customerId, firstName, lastName, address, password);
                    bank.openAccount(customerId, accountType, initialDeposit);
                    bank.saveToFile(DATA_FILE);

                    showAlert("Success",
                            "Account created successfully!\n\n" +
                                    "Customer ID: " + customerId + "\n" +
                                    "Name: " + firstName + " " + lastName + "\n" +
                                    "Account Type: " + accountType + "\n" +
                                    "Initial Deposit: $" + initialDeposit + "\n\n" +
                                    "You can now login with your credentials.");

                } catch (Exception e) {
                    showAlert("Error", "Failed to create account: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}