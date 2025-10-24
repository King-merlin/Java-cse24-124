package com.banking.controller;

import com.banking.view.LoginView;
import javafx.stage.Stage;

public class LoginController {
    private LoginView view;
    private Stage stage;

    // Default credentials
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin123";

    public LoginController(Stage stage) {
        this.stage = stage;
        this.view = new LoginView(stage);

        view.show();
        initializeHandlers();
        stage.show();
    }

    private void initializeHandlers() {
        view.getBtnLogin().setOnAction(e -> handleLogin());
        view.getBtnExit().setOnAction(e -> System.exit(0));

        // Allow Enter key to login
        view.getTxtPassword().setOnAction(e -> handleLogin());
    }

    private void handleLogin() {
        String username = view.getTxtUsername().getText().trim();
        String password = view.getTxtPassword().getText();

        if (username.isEmpty() || password.isEmpty()) {
            view.displayMessage("Please enter username and password", true);
            return;
        }

        if (username.equals(DEFAULT_USERNAME) && password.equals(DEFAULT_PASSWORD)) {
            view.displayMessage("Login successful!", false);
            // Proceed to main menu
            new MainMenuController(stage);
        } else {
            view.displayMessage("Invalid username or password", true);
            view.getTxtPassword().clear();
        }
    }
}