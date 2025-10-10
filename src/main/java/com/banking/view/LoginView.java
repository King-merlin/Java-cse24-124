package com.banking.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginView {
    private Stage stage;
    private TextField txtUsername;
    private PasswordField txtPassword;
    private Button btnLogin;
    private Button btnExit;
    private Label lblMessage;

    public LoginView(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #667eea 0%, #764ba2 100%);");

        // Title
        Label title = new Label("Banking System");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitle = new Label("Secure Login");
        subtitle.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        // Login Form Panel
        VBox formPanel = new VBox(15);
        formPanel.setPadding(new Insets(30));
        formPanel.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        formPanel.setMaxWidth(350);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(15);
        form.setAlignment(Pos.CENTER);

        Label lblUsername = new Label("Username:");
        lblUsername.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Label lblPassword = new Label("Password:");
        lblPassword.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        txtUsername = new TextField();
        txtUsername.setPromptText("Enter username");
        txtUsername.setPrefWidth(250);
        txtUsername.setStyle("-fx-font-size: 14px;");

        txtPassword = new PasswordField();
        txtPassword.setPromptText("Enter password");
        txtPassword.setPrefWidth(250);
        txtPassword.setStyle("-fx-font-size: 14px;");

        form.add(lblUsername, 0, 0);
        form.add(txtUsername, 0, 1);
        form.add(lblPassword, 0, 2);
        form.add(txtPassword, 0, 3);

        // Buttons
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);

        btnLogin = new Button("Login");
        btnLogin.setPrefWidth(120);
        btnLogin.setStyle("-fx-background-color: #667eea; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");

        btnExit = new Button("Exit");
        btnExit.setPrefWidth(120);
        btnExit.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");

        buttons.getChildren().addAll(btnLogin, btnExit);

        // Message Label
        lblMessage = new Label("");
        lblMessage.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 12px;");
        lblMessage.setAlignment(Pos.CENTER);

        formPanel.getChildren().addAll(form, buttons, lblMessage);
        layout.getChildren().addAll(title, subtitle, formPanel);

        Scene scene = new Scene(layout, 500, 450);
        stage.setScene(scene);
        stage.setTitle("Banking System - Login");
    }

    // Getters
    public TextField getTxtUsername() { return txtUsername; }
    public PasswordField getTxtPassword() { return txtPassword; }
    public Button getBtnLogin() { return btnLogin; }
    public Button getBtnExit() { return btnExit; }
    public Label getLblMessage() { return lblMessage; }

    public void clearForm() {
        txtUsername.clear();
        txtPassword.clear();
        lblMessage.setText("");
    }

    public void displayMessage(String message, boolean isError) {
        lblMessage.setText(message);
        if (isError) {
            lblMessage.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 12px;");
        } else {
            lblMessage.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 12px;");
        }
    }
}