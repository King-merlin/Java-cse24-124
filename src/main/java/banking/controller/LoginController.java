package banking.controller;

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
        Customer c = bank.getCustomer(id);
        if (c != null && c.checkPassword(pw)) {
            lblStatus.setText("Login successful as Customer!");
            openCustomerView(c);
        } else {
            lblStatus.setText("Invalid customer credentials.");
        }
    }

    @FXML
    public void handleStaffLogin(ActionEvent e) {
        if (txtUserId.getText().equals("admin") && txtPassword.getText().equals("admin123")) {
            lblStatus.setText("Staff login successful.");
            openStaffView();
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
        Customer newCust = bank.createCustomer(id, "New", "Customer", "Gaborone", "1234");
        bank.openAccount(id, "SAVINGS", 1000);
        lblStatus.setText("Created new customer with default savings account!");
        bank.saveToFile(DATA_FILE);
    }

    private void openCustomerView(Customer c) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/banking/view/CustomerView.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            CustomerController controller = loader.getController();
            controller.setCustomerAndBank(c, bank);
            stage.setTitle("Customer Dashboard - " + c.getName());
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void openStaffView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/banking/view/StaffView.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            StaffController controller = loader.getController();
            controller.setBank(bank);
            stage.setTitle("Staff Dashboard");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
