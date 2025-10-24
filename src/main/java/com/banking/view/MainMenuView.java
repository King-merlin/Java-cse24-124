package com.banking.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainMenuView {
    private Stage stage;
    private Button btnCustomer;
    private Button btnAccount;
    private Button btnTransaction;
    private Button btnInterest;
    private Button btnLogout;
    private Label lblMessage;

    public MainMenuView(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #ecf0f1;");

        // Top Bar
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: #2c3e50;");

        Label title = new Label("Banking System - Main Menu");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        topBar.getChildren().add(title);
        layout.setTop(topBar);

        // Center Content
        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(50));
        centerContent.setAlignment(Pos.CENTER);

        Label welcome = new Label("Welcome to Banking System");
        welcome.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane menu = new GridPane();
        menu.setHgap(20);
        menu.setVgap(20);
        menu.setAlignment(Pos.CENTER);

        btnCustomer = createMenuButton("Customer Management", "#3498db");
        btnAccount = createMenuButton("Account Management", "#2ecc71");
        btnTransaction = createMenuButton("Transactions", "#e67e22");
        btnInterest = createMenuButton("Calculate Interest", "#9b59b6");
        btnLogout = createMenuButton("Logout", "#e74c3c");

        menu.add(btnCustomer, 0, 0);
        menu.add(btnAccount, 1, 0);
        menu.add(btnTransaction, 0, 1);
        menu.add(btnInterest, 1, 1);
        menu.add(btnLogout, 0, 2, 2, 1);

        lblMessage = new Label("");
        lblMessage.setStyle("-fx-font-size: 14px;");

        centerContent.getChildren().addAll(welcome, menu, lblMessage);
        layout.setCenter(centerContent);

        Scene scene = new Scene(layout, 700, 500);
        stage.setScene(scene);
        stage.setTitle("Banking System - Main Menu");
    }

    private Button createMenuButton(String text, String color) {
        Button btn = new Button(text);
        btn.setPrefSize(250, 80);
        btn.setStyle("-fx-background-color: " + color + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 10;");
        return btn;
    }

    public void showMessage(String message, boolean isError) {
        lblMessage.setText(message);
        lblMessage.setStyle("-fx-text-fill: " + (isError ? "#e74c3c" : "#27ae60") +
                "; -fx-font-size: 14px;");
    }

    // Getters
    public Button getBtnCustomer() { return btnCustomer; }
    public Button getBtnAccount() { return btnAccount; }
    public Button getBtnTransaction() { return btnTransaction; }
    public Button getBtnInterest() { return btnInterest; }
    public Button getBtnLogout() { return btnLogout; }
}
