package banking.controller;

import banking.model.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CustomerController {
    @FXML private TextArea txtArea;

    private Customer customer;
    private Bank bank;
    private static final String DATA_FILE = "BankData.txt";

    public void setCustomerAndBank(Customer c, Bank b) {
        this.customer = c;
        this.bank = b;
        txtArea.appendText("Welcome " + c.getName() + "!\n\n");
        listAccounts();
    }

    @FXML
    public void listAccounts() {
        txtArea.clear();
        txtArea.appendText("=== Your Accounts ===\n\n");

        if (customer.getAccounts().isEmpty()) {
            txtArea.appendText("No accounts found.\n");
            return;
        }

        for (Account a : customer.getAccounts()) {
            txtArea.appendText(String.format("Account: %s\nType: %s\nBalance: $%.2f\n\n",
                    a.getAccountNumber(),
                    a.getClass().getSimpleName().replace("Account", ""),
                    a.getBalance()));
        }
    }

    @FXML
    public void deposit() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Deposit Money");
        dialog.setHeaderText("Enter deposit details");

        ButtonType depositButtonType = new ButtonType("Deposit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(depositButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<String> accountCombo = new ComboBox<>();
        for (Account a : customer.getAccounts()) {
            accountCombo.getItems().add(a.getAccountNumber() + " (Balance: $" +
                    String.format("%.2f", a.getBalance()) + ")");
        }
        if (!accountCombo.getItems().isEmpty()) {
            accountCombo.getSelectionModel().selectFirst();
        }

        TextField amountField = new TextField();
        amountField.setPromptText("e.g., 500.00");

        grid.add(new Label("Select Account:"), 0, 0);
        grid.add(accountCombo, 1, 0);
        grid.add(new Label("Amount:"), 0, 1);
        grid.add(amountField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(response -> {
            if (response == depositButtonType) {
                try {
                    String selected = accountCombo.getValue();
                    if (selected == null) {
                        txtArea.appendText("Error: Please select an account.\n");
                        return;
                    }

                    String accNum = selected.split(" ")[0];
                    Account a = findAccount(accNum);

                    if (a == null) {
                        txtArea.appendText("Error: Account not found.\n");
                        return;
                    }

                    double amt = Double.parseDouble(amountField.getText().trim());
                    a.deposit(amt);
                    txtArea.appendText(String.format("Successfully deposited $%.2f to %s\nNew balance: $%.2f\n\n",
                            amt, accNum, a.getBalance()));
                    bank.saveToFile(DATA_FILE);
                    listAccounts();
                } catch (NumberFormatException e) {
                    txtArea.appendText("Error: Invalid amount.\n");
                }
            }
        });
    }

    @FXML
    public void withdraw() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Withdraw Money");
        dialog.setHeaderText("Enter withdrawal details");

        ButtonType withdrawButtonType = new ButtonType("Withdraw", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(withdrawButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<String> accountCombo = new ComboBox<>();
        for (Account a : customer.getAccounts()) {
            accountCombo.getItems().add(a.getAccountNumber() + " (" +
                    a.getClass().getSimpleName().replace("Account", "") +
                    ") - $" + String.format("%.2f", a.getBalance()));
        }
        if (!accountCombo.getItems().isEmpty()) {
            accountCombo.getSelectionModel().selectFirst();
        }

        TextField amountField = new TextField();
        amountField.setPromptText("e.g., 200.00");

        grid.add(new Label("Select Account:"), 0, 0);
        grid.add(accountCombo, 1, 0);
        grid.add(new Label("Amount:"), 0, 1);
        grid.add(amountField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(response -> {
            if (response == withdrawButtonType) {
                try {
                    String selected = accountCombo.getValue();
                    if (selected == null) {
                        txtArea.appendText("Error: Please select an account.\n");
                        return;
                    }

                    String accNum = selected.split(" ")[0];
                    Account a = findAccount(accNum);

                    if (a == null) {
                        txtArea.appendText("Error: Account not found.\n");
                        return;
                    }

                    double amt = Double.parseDouble(amountField.getText().trim());
                    boolean success = a.withdraw(amt);

                    if (success) {
                        txtArea.appendText(String.format("Successfully withdrew $%.2f from %s\nNew balance: $%.2f\n\n",
                                amt, accNum, a.getBalance()));
                        bank.saveToFile(DATA_FILE);
                        listAccounts();
                    } else {
                        txtArea.appendText("Withdrawal failed. Check account type and balance.\n");
                    }
                } catch (NumberFormatException e) {
                    txtArea.appendText("Error: Invalid amount.\n");
                }
            }
        });
    }

    private Account findAccount(String acc) {
        for (Account a : customer.getAccounts()) {
            if (a.getAccountNumber().equals(acc)) return a;
        }
        return null;
    }

    @FXML
    public void saveAndExit() {
        bank.saveToFile(DATA_FILE);
        Stage stage = (Stage) txtArea.getScene().getWindow();
        stage.close();
    }
}