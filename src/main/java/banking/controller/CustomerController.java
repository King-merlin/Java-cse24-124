package banking.controller;

import banking.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CustomerController {
    @FXML private TextArea txtArea;

    private Customer customer;
    private Bank bank;
    private static final String DATA_FILE = "BankData.txt";

    public void setCustomerAndBank(Customer c, Bank b) {
        this.customer = c;
        this.bank = b;
        txtArea.appendText("Welcome " + c.getName() + "!\n");
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
        String acc = dialog.showAndWait().orElse("");
        Account a = findAccount(acc);
        if (a == null) { txtArea.appendText("No such account.\n"); return; }

        TextInputDialog amountDialog = new TextInputDialog();
        amountDialog.setHeaderText("Enter amount to deposit:");
        double amt = Double.parseDouble(amountDialog.showAndWait().orElse("0"));
        a.deposit(amt);
        txtArea.appendText("Deposited " + amt + " into " + a.getAccountNumber() + "\n");
        bank.saveToFile(DATA_FILE);
    }

    @FXML
    public void withdraw() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Enter account number:");
        String acc = dialog.showAndWait().orElse("");
        Account a = findAccount(acc);
        if (a == null) { txtArea.appendText("No such account.\n"); return; }

        TextInputDialog amountDialog = new TextInputDialog();
        amountDialog.setHeaderText("Enter amount to withdraw:");
        double amt = Double.parseDouble(amountDialog.showAndWait().orElse("0"));
        a.withdraw(amt);
        txtArea.appendText("Attempted withdrawal of " + amt + " from " + a.getAccountNumber() + "\n");
        bank.saveToFile(DATA_FILE);
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
