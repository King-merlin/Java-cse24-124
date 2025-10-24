package com.banking.controller;

import com.banking.model.Bank;
import com.banking.view.MainMenuView;
import javafx.stage.Stage;

public class MainMenuController {
    private MainMenuView view;
    private Stage stage;
    private Bank bank;

    public MainMenuController(Stage stage) {
        this.stage = stage;
        this.bank = new Bank("First National Bank");
        this.view = new MainMenuView(stage);

        initializeHandlers();
        view.show();
    }

    private void initializeHandlers() {
        view.getBtnCustomer().setOnAction(e ->
                new CustomerController(stage, bank, this)
        );

        view.getBtnAccount().setOnAction(e ->
                new AccountController(stage, bank, this)
        );

        view.getBtnTransaction().setOnAction(e ->
                new TransactionController(stage, bank, this)
        );

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
    }
}
