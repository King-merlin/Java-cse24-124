package com.banking.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AccountView {
    private Stage stage;
    private ComboBox<String> cmbAccountType;
    private ComboBox<String> cmbCustomer;
    private TextField txtInitialDeposit;
    private TextField txtBranch;
    private TextField txtEmployer;
    private TextField txtEmployerAddress;
    private Button btnOpenAccount;
    private Button btnClear;
    private Button btnBack;
    private TableView<String> tblAccounts;
    private VBox employerPanel;

    public AccountView(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #ecf0f1;");

        // Top Bar
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: #34495e;");

        Label title = new Label("Account Management");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        topBar.getChildren().add(title);
        layout.setTop(topBar);

        // Center Content
        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(30));

        // Form Panel
        VBox formPanel = new VBox(15);
        formPanel.setPadding(new Insets(20));
        formPanel.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        Label formTitle = new Label("Open New Account");
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);

        cmbAccountType = new ComboBox<>();
        cmbAccountType.getItems().addAll("Savings", "Investment", "Cheque");
        cmbAccountType.setPromptText("Select Account Type");
        cmbAccountType.setPrefWidth(250);

        cmbCustomer = new ComboBox<>();
        cmbCustomer.setPromptText("Select Customer");
        cmbCustomer.setPrefWidth(250);

        txtInitialDeposit = new TextField();
        txtInitialDeposit.setPromptText("Enter amount (BWP)");

        txtBranch = new TextField();
        txtBranch.setPromptText("e.g., Gaborone Main");

        form.add(createLabel("Account Type:"), 0, 0);
        form.add(cmbAccountType, 1, 0);
        form.add(createLabel("Customer:"), 0, 1);
        form.add(cmbCustomer, 1, 1);
        form.add(createLabel("Initial Deposit:"), 0, 2);
        form.add(txtInitialDeposit, 1, 2);
        form.add(createLabel("Branch:"), 0, 3);
        form.add(txtBranch, 1, 3);

        // Employer Panel (for Cheque accounts)
        employerPanel = new VBox(10);
        employerPanel.setPadding(new Insets(15));
        employerPanel.setStyle("-fx-background-color: #e8f5e9; -fx-border-color: #4caf50; -fx-border-radius: 5; -fx-background-radius: 5;");
        employerPanel.setVisible(false);
        employerPanel.setManaged(false);

        Label empTitle = new Label("Employment Information (Required for Cheque Account)");
        empTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");

        GridPane empForm = new GridPane();
        empForm.setHgap(15);
        empForm.setVgap(10);

        txtEmployer = new TextField();
        txtEmployer.setPromptText("Company name");
        txtEmployerAddress = new TextField();
        txtEmployerAddress.setPromptText("Company address");

        empForm.add(createLabel("Employer:"), 0, 0);
        empForm.add(txtEmployer, 1, 0);
        empForm.add(createLabel("Employer Address:"), 0, 1);
        empForm.add(txtEmployerAddress, 1, 1);

        employerPanel.getChildren().addAll(empTitle, empForm);

        // Buttons
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);

        btnOpenAccount = new Button("Open Account");
        btnOpenAccount.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        btnOpenAccount.setPrefWidth(150);

        btnClear = new Button("Clear");
        btnClear.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        btnClear.setPrefWidth(100);

        btnBack = new Button("Back");
        btnBack.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        btnBack.setPrefWidth(100);

        buttons.getChildren().addAll(btnOpenAccount, btnClear, btnBack);

        formPanel.getChildren().addAll(formTitle, form, employerPanel, buttons);

        // Account List Panel
        VBox listPanel = new VBox(10);
        listPanel.setPadding(new Insets(20));
        listPanel.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        Label listTitle = new Label("Opened Accounts");
        listTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        tblAccounts = new TableView<>();
        tblAccounts.setPrefHeight(200);

        listPanel.getChildren().addAll(listTitle, tblAccounts);

        centerContent.getChildren().addAll(formPanel, listPanel);
        layout.setCenter(new ScrollPane(centerContent));

        Scene scene = new Scene(layout, 750, 700);
        stage.setScene(scene);
        stage.setTitle("Banking System - Account Management");
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        return label;
    }

    // Getters
    public ComboBox<String> getCmbAccountType() { return cmbAccountType; }
    public ComboBox<String> getCmbCustomer() { return cmbCustomer; }
    public TextField getTxtInitialDeposit() { return txtInitialDeposit; }
    public TextField getTxtBranch() { return txtBranch; }
    public TextField getTxtEmployer() { return txtEmployer; }
    public TextField getTxtEmployerAddress() { return txtEmployerAddress; }
    public Button getBtnOpenAccount() { return btnOpenAccount; }
    public Button getBtnClear() { return btnClear; }
    public Button getBtnBack() { return btnBack; }
    public TableView<String> getTblAccounts() { return tblAccounts; }
    public VBox getEmployerPanel() { return employerPanel; }

    public void clearForm() {
        cmbAccountType.setValue(null);
        cmbCustomer.setValue(null);
        txtInitialDeposit.clear();
        txtBranch.clear();
        txtEmployer.clear();
        txtEmployerAddress.clear();
    }
}