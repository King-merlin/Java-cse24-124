package com.banking.controller;

import com.banking.model.Bank;
import com.banking.model.IndividualCustomer;
import com.banking.view.CustomerView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class CustomerController {
    private CustomerView view;
    private Bank bank;
    private MainMenuController mainController;
    private ObservableList<IndividualCustomer> customerList;

    public CustomerController(Stage stage, Bank bank, MainMenuController mainController) {
        this.view = new CustomerView(stage);
        this.bank = bank;
        this.mainController = mainController;
        this.customerList = FXCollections.observableArrayList();

        initializeHandlers();
        view.show();
    }

    private void initializeHandlers() {
        view.getBtnSave().setOnAction(e -> handleSaveCustomer());
        view.getBtnClear().setOnAction(e -> view.clearForm());
        view.getBtnBack().setOnAction(e -> mainController.returnToMenu());
    }

    private void handleSaveCustomer() {
        try {
            String customerId = view.getTxtCustomerId().getText();
            String firstName = view.getTxtFirstName().getText();
            String lastName = view.getTxtLastName().getText();
            String address = view.getTxtAddress().getText();

            // Validation
            if (customerId == null || customerId.trim().isEmpty()) {
                showError("Please enter Customer ID");
                return;
            }

            if (firstName == null || firstName.trim().isEmpty()) {
                showError("Please enter First Name");
                return;
            }

            if (lastName == null || lastName.trim().isEmpty()) {
                showError("Please enter Last Name");
                return;
            }

            if (address == null || address.trim().isEmpty()) {
                showError("Please enter Address");
                return;
            }

            // Check for duplicate customer ID
            if (bank.findCustomer(customerId.trim()) != null) {
                showError("Customer ID already exists");
                return;
            }

            IndividualCustomer customer = new IndividualCustomer(
                    customerId.trim(),
                    firstName.trim(),
                    lastName.trim(),
                    address.trim()
            );

            bank.addCustomer(customer);
            customerList.add(customer);

            view.clearForm();
            showSuccess(String.format(
                    "Customer registered successfully!%n%nCustomer ID: %s%nName: %s",
                    customer.getCustomerId(),
                    customer.getFullName()
            ));

        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        } catch (Exception ex) {
            showError("Unexpected error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
