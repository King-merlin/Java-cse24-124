package com.banking.controller;

import com.banking.model.*;
import javafx.stage.Stage;

public class TransactionController {
    private Bank bank;
    private MainMenuController mainController;

    public TransactionController(Stage stage, Bank bank, MainMenuController mainController) {
        this.bank = bank;
        this.mainController = mainController;

        // Implement transaction view and logic here
        // For now, return to menu
        mainController.returnToMenu();
    }
}
