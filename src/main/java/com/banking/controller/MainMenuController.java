package com.banking.controller;

import com.banking.model.Bank;
import com.banking.view.MainMenuView;
import javafx.stage.Stage;

public class MainMenuController {
    private MainMenuView view;
    private Stage stage;
    private Bank bank;

    // Keep references to child controllers
    private CustomerController customerController;
    private AccountController accountController;

    public MainMenuController(Stage stage) {
        this.stage = stage;
        this.bank = new Bank("First National Bank");
        this.view = new MainMenuView(stage);

        view.show();
        initializeHandlers();
    }

    private void initializeHandlers() {
        view.getBtnCustomer().setOnAction(e -> {
            if (customerController == null) {
                customerController = new CustomerController(stage, bank, this);
            } else {
                customerController.show();
            }
        });

        view.getBtnAccount().setOnAction(e -> {
            if (accountController == null) {
                accountController = new AccountController(stage, bank, this);
            } else {
                accountController.show();
            }
        });

        view.getBtnTransaction().setOnAction(e -> {
            new TransactionController(stage, bank, this);
        });

        view.getBtnInterest().setOnAction(e -> handleCalculateInterest());

        view.getBtnLogout().setOnAction(e -> {
            new LoginController(stage);
        });
    }

    private void handleCalculateInterest() {
        bank.calculateMonthlyInterest();
        view.showMessage("Monthly interest calculated successfully!", false);
    }

    public void returnToMenu() {
        view.show();
        // Re-initialize handlers when returning to menu
        initializeHandlers();
    }
}