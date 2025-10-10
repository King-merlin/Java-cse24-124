package com.banking.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class CustomerView {
    private Stage stage;
    private TextField txtCustomerId;
    private TextField txtFirstName;
    private TextField txtLastName;
    private TextField txtAddress;
    private Button btnSave;
    private Button btnClear;
    private Button btnBack;
    private TableView<String> tblCustomers;

    public CustomerView(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #ecf0f1;");

        // Top Bar
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: #34495e;");

        Label title = new Label("Customer Registration");
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

        Label formTitle = new Label("New Customer Information");
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);

        txtCustomerId = new TextField();
        txtCustomerId.setPromptText("e.g., CUST001");
        txtFirstName = new TextField();
        txtFirstName.setPromptText("Enter first name");
        txtLastName = new TextField();
        txtLastName.setPromptText("Enter last name");
        txtAddress = new TextField();
        txtAddress.setPromptText("Enter address");

        form.add(createLabel("Customer ID:"), 0, 0);
        form.add(txtCustomerId, 1, 0);
        form.add(createLabel("First Name:"), 0, 1);
        form.add(txtFirstName, 1, 1);
        form.add(createLabel("Last Name:"), 0, 2);
        form.add(txtLastName, 1, 2);
        form.add(createLabel("Address:"), 0, 3);
        form.add(txtAddress, 1, 3);

        // Buttons
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);

        btnSave = new Button("Save Customer");
        btnSave.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        btnSave.setPrefWidth(150);

        btnClear = new Button("Clear");
        btnClear.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        btnClear.setPrefWidth(100);

        btnBack = new Button("Back");
        btnBack.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        btnBack.setPrefWidth(100);

        buttons.getChildren().addAll(btnSave, btnClear, btnBack);

        formPanel.getChildren().addAll(formTitle, form, buttons);

        // Customer List Panel
        VBox listPanel = new VBox(10);
        listPanel.setPadding(new Insets(20));
        listPanel.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        Label listTitle = new Label("Registered Customers");
        listTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        tblCustomers = new TableView<>();
        tblCustomers.setPrefHeight(200);

        listPanel.getChildren().addAll(listTitle, tblCustomers);

        centerContent.getChildren().addAll(formPanel, listPanel);
        layout.setCenter(new ScrollPane(centerContent));

        Scene scene = new Scene(layout, 700, 650);
        stage.setScene(scene);
        stage.setTitle("Banking System - Customer Management");
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        return label;
    }

    // Getters
    public TextField getTxtCustomerId() { return txtCustomerId; }
    public TextField getTxtFirstName() { return txtFirstName; }
    public TextField getTxtLastName() { return txtLastName; }
    public TextField getTxtAddress() { return txtAddress; }
    public Button getBtnSave() { return btnSave; }
    public Button getBtnClear() { return btnClear; }
    public Button getBtnBack() { return btnBack; }
    public TableView<String> getTblCustomers() { return tblCustomers; }

    public void clearForm() {
        txtCustomerId.clear();
        txtFirstName.clear();
        txtLastName.clear();
        txtAddress.clear();
    }
}