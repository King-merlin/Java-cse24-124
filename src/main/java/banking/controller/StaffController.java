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
        txtArea.appendText("Staff logged in successfully.\n");
    }

    @FXML
    public void listCustomers() {
        txtArea.clear();
        for (Customer c : bank.getAllCustomers()) {
            txtArea.appendText(c + " (" + c.getAccounts().size() + " accounts)\n");
        }
    }

    @FXML
    public void calculateInterest() {
        bank.calculateInterestForAllCustomers();
        txtArea.appendText("Interest applied to all accounts.\n");
        bank.saveToFile(DATA_FILE);
    }

    @FXML
    public void saveAndExit() {
        bank.saveToFile(DATA_FILE);
        Stage stage = (Stage) txtArea.getScene().getWindow();
        stage.close();
    }
}
