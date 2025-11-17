package banking.controller;

import banking.MainApp;
import banking.model.Bank;
import banking.model.Customer;
import banking.model.Account;
import banking.model.SessionManager;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class CustomerDashboardController {

    @FXML private TextArea txtArea;
    private Bank bank = Bank.loadFromFile("BankData.txt");
    private Customer customer;

    @FXML
    public void initialize() {
        customer = SessionManager.getCustomer();
        txtArea.appendText("Welcome, " + customer.getName() + "!\n\n");
        listAccounts();
    }

    @FXML
    public void listAccounts() {
        txtArea.clear();
        txtArea.appendText("=== Your Accounts ===\n\n");

        if (customer.getAccounts().isEmpty()) {
            txtArea.appendText("No accounts found.\n");
            txtArea.appendText("\nWould you like to open a new account?\n");
            txtArea.appendText("Click 'Open New Account' button below.\n");
            return;
        }

        for (Account a : customer.getAccounts()) {
            String accountType = a.getClass().getSimpleName().replace("Account", "");
            txtArea.appendText(String.format("╔══════════════════════════════════╗\n"));
            txtArea.appendText(String.format("║ Account Number: %-16s ║\n", a.getAccountNumber()));
            txtArea.appendText(String.format("║ Type: %-26s ║\n", accountType));
            txtArea.appendText(String.format("║ Balance: $%-22.2f ║\n", a.getBalance()));
            txtArea.appendText(String.format("╚══════════════════════════════════╝\n\n"));
        }
    }

    @FXML
    public void openNewAccount() {
        // Dialog to open a new account for existing customer
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Open New Account");
        dialog.setHeaderText("Add a new account to your profile");

        ButtonType createButtonType = new ButtonType("Open Account", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Account type selection
        ComboBox<String> accountTypeCombo = new ComboBox<>();
        accountTypeCombo.getItems().addAll("SAVINGS", "CHEQUE", "INVESTMENT");
        accountTypeCombo.setValue("SAVINGS");

        TextField depositField = new TextField();
        depositField.setPromptText("Initial deposit");
        depositField.setText("1000");

        Label infoLabel = new Label(
                "Account Types:\n" +
                        "• Savings: No withdrawals allowed, earns 0.05% monthly interest\n" +
                        "• Cheque: Full withdrawals allowed, no interest earned\n" +
                        "• Investment: Min $500 balance, earns 5% monthly interest"
        );
        infoLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #666;");
        infoLabel.setWrapText(true);
        infoLabel.setMaxWidth(300);

        grid.add(new Label("Account Type:"), 0, 0);
        grid.add(accountTypeCombo, 1, 0);
        grid.add(new Label("Initial Deposit:"), 0, 1);
        grid.add(depositField, 1, 1);
        grid.add(infoLabel, 0, 2, 2, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(response -> {
            if (response == createButtonType) {
                try {
                    String accountType = accountTypeCombo.getValue();
                    double initialDeposit = Double.parseDouble(depositField.getText().trim());

                    if (initialDeposit <= 0) {
                        showAlert("Error", "Initial deposit must be greater than zero.");
                        return;
                    }

                    if (accountType.equals("INVESTMENT") && initialDeposit < 500) {
                        showAlert("Error", "Investment accounts require a minimum deposit of $500.");
                        return;
                    }

                    bank.openAccount(customer.getCustomerId(), accountType, initialDeposit);
                    bank.saveToFile("BankData.txt");

                    showAlert("Success",
                            String.format("New %s account opened!\nInitial deposit: $%.2f",
                                    accountType, initialDeposit));

                    // Reload customer data
                    customer = bank.getCustomer(customer.getCustomerId());
                    SessionManager.setCustomer(customer);
                    listAccounts();

                } catch (NumberFormatException e) {
                    showAlert("Error", "Please enter a valid deposit amount.");
                } catch (Exception e) {
                    showAlert("Error", "Failed to open account: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    public void deposit() {
        if (customer.getAccounts().isEmpty()) {
            showAlert("No Accounts", "You don't have any accounts yet. Please open an account first.");
            return;
        }

        // Create custom dialog with all fields in one screen
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Deposit Money");
        dialog.setHeaderText("Select account and enter deposit amount");

        ButtonType depositButtonType = new ButtonType("Deposit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(depositButtonType, ButtonType.CANCEL);

        // Create the form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // ComboBox for account selection
        ComboBox<String> accountCombo = new ComboBox<>();
        for (Account a : customer.getAccounts()) {
            String accountType = a.getClass().getSimpleName().replace("Account", "");
            accountCombo.getItems().add(String.format("%s (%s) - $%.2f",
                    a.getAccountNumber(), accountType, a.getBalance()));
        }
        accountCombo.getSelectionModel().selectFirst();

        TextField amountField = new TextField();
        amountField.setPromptText("e.g., 500.00");

        grid.add(new Label("Select Account:"), 0, 0);
        grid.add(accountCombo, 1, 0);
        grid.add(new Label("Amount to Deposit:"), 0, 1);
        grid.add(amountField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        amountField.requestFocus();

        dialog.showAndWait().ifPresent(response -> {
            if (response == depositButtonType) {
                try {
                    String selected = accountCombo.getValue();
                    if (selected == null) {
                        showAlert("Error", "Please select an account.");
                        return;
                    }

                    String accNum = selected.split(" ")[0];
                    Account account = findAccount(accNum);

                    if (account == null) {
                        showAlert("Error", "Account not found.");
                        return;
                    }

                    double amount = Double.parseDouble(amountField.getText().trim());

                    if (amount <= 0) {
                        showAlert("Error", "Amount must be greater than zero.");
                        return;
                    }

                    account.deposit(amount);
                    bank.saveToFile("BankData.txt");

                    showAlert("Success",
                            String.format("Successfully deposited $%.2f\nNew balance: $%.2f",
                                    amount, account.getBalance()));

                    listAccounts(); // Refresh the display

                } catch (NumberFormatException e) {
                    showAlert("Error", "Please enter a valid amount.");
                } catch (Exception e) {
                    showAlert("Error", "Deposit failed: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    public void withdraw() {
        if (customer.getAccounts().isEmpty()) {
            showAlert("No Accounts", "You don't have any accounts yet. Please open an account first.");
            return;
        }

        // Create custom dialog with all fields in one screen
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Withdraw Money");
        dialog.setHeaderText("Select account and enter withdrawal amount");

        ButtonType withdrawButtonType = new ButtonType("Withdraw", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(withdrawButtonType, ButtonType.CANCEL);

        // Create the form
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        // ComboBox for account selection
        ComboBox<String> accountCombo = new ComboBox<>();
        for (Account a : customer.getAccounts()) {
            String accountType = a.getClass().getSimpleName().replace("Account", "");
            accountCombo.getItems().add(String.format("%s (%s) - Balance: $%.2f",
                    a.getAccountNumber(), accountType, a.getBalance()));
        }
        accountCombo.getSelectionModel().selectFirst();
        accountCombo.setPrefWidth(350);

        TextField amountField = new TextField();
        amountField.setPromptText("e.g., 200.00");

        Label infoLabel = new Label(
                "⚠ Withdrawal Rules:\n" +
                        "• Savings accounts: NO withdrawals allowed\n" +
                        "• Cheque accounts: Full withdrawal allowed\n" +
                        "• Investment accounts: Must keep minimum $500 balance"
        );
        infoLabel.setStyle("-fx-background-color: #fff3cd; -fx-padding: 10; " +
                "-fx-border-color: #ffc107; -fx-border-radius: 5; " +
                "-fx-background-radius: 5; -fx-font-size: 11px;");
        infoLabel.setWrapText(true);
        infoLabel.setMaxWidth(350);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Select Account:"), 0, 0);
        grid.add(accountCombo, 1, 0);
        grid.add(new Label("Amount to Withdraw:"), 0, 1);
        grid.add(amountField, 1, 1);

        vbox.getChildren().addAll(grid, infoLabel);
        dialog.getDialogPane().setContent(vbox);
        amountField.requestFocus();

        dialog.showAndWait().ifPresent(response -> {
            if (response == withdrawButtonType) {
                try {
                    String selected = accountCombo.getValue();
                    if (selected == null) {
                        showAlert("Error", "Please select an account.");
                        return;
                    }

                    String accNum = selected.split(" ")[0];
                    Account account = findAccount(accNum);

                    if (account == null) {
                        showAlert("Error", "Account not found.");
                        return;
                    }

                    double amount = Double.parseDouble(amountField.getText().trim());

                    if (amount <= 0) {
                        showAlert("Error", "Amount must be greater than zero.");
                        return;
                    }

                    boolean success = account.withdraw(amount);

                    if (success) {
                        bank.saveToFile("BankData.txt");
                        showAlert("Success",
                                String.format("Successfully withdrew $%.2f\nNew balance: $%.2f",
                                        amount, account.getBalance()));
                        listAccounts(); // Refresh the display
                    } else {
                        String accountType = account.getClass().getSimpleName().replace("Account", "");
                        String reason = "";

                        if (accountType.equals("Savings")) {
                            reason = "Savings accounts do not allow withdrawals.";
                        } else if (accountType.equals("Investment")) {
                            reason = "Investment accounts must maintain a minimum balance of $500.";
                        } else {
                            reason = "Insufficient funds in your account.";
                        }

                        showAlert("Withdrawal Failed", reason);
                    }

                } catch (NumberFormatException e) {
                    showAlert("Error", "Please enter a valid amount.");
                } catch (Exception e) {
                    showAlert("Error", "Withdrawal failed: " + e.getMessage());
                }
            }
        });
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}