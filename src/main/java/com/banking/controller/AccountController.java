package com.banking.controller;

import com.banking.model.*;
import com.banking.view.AccountView;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import java.util.HashMap;
import java.util.Map;

public class AccountController {
    private AccountView view;
    private Bank bank;
    private MainMenuController mainController;
    private Map<String, Customer> customerMap;

    public AccountController(Stage stage, Bank bank, MainMenuController mainController) {
        this.view = new AccountView(stage);
        this.bank = bank;
        this.mainController = mainController;
        this.customerMap = new HashMap<>();

        initializeHandlers();
        loadCustomers();
        view.show();
    }

    private void initializeHandlers() {
        view.getCmbAccountType().setOnAction(e -> handleAccountTypeChange());
        view.getBtnOpenAccount().setOnAction(e -> handleOpenAccount());
        view.getBtnClear().setOnAction(e -> view.clearForm());
        view.getBtnBack().setOnAction(e -> mainController.returnToMenu());
    }

    private void handleAccountTypeChange() {
        String accountType = view.getCmbAccountType().getValue();
        boolean showEmployer = accountType != null && accountType.equalsIgnoreCase("Cheque");
        view.getEmployerPanel().setVisible(showEmployer);
        view.getEmployerPanel().setManaged(showEmployer);
    }

    private void loadCustomers() {
        view.getCmbCustomer().getItems().clear();
        customerMap.clear();

        for (Customer customer : bank.getCustomers()) {
            String displayText = customer.getCustomerId() + " - " + customer.getFullName();
            view.getCmbCustomer().getItems().add(displayText);
            customerMap.put(displayText, customer);
        }
    }

    private void handleOpenAccount() {
        try {
            String accountType = view.getCmbAccountType().getValue();
            String customerSelection = view.getCmbCustomer().getValue();
            String depositText = view.getTxtInitialDeposit().getText();
            String branch = view.getTxtBranch().getText();

            // Validation
            if (accountType == null || accountType.trim().isEmpty()) {
                showError("Please select an account type");
                return;
            }

            if (customerSelection == null || customerSelection.trim().isEmpty()) {
                showError("Please select a customer");
                return;
            }

            if (depositText == null || depositText.trim().isEmpty()) {
                showError("Please enter initial deposit amount");
                return;
            }

            if (branch == null || branch.trim().isEmpty()) {
                showError("Please enter branch name");
                return;
            }

            double initialDeposit = Double.parseDouble(depositText.trim());

            if (initialDeposit < 0) {
                showError("Initial deposit cannot be negative");
                return;
            }

            Customer customer = customerMap.get(customerSelection);
            if (customer == null) {
                showError("Customer not found");
                return;
            }

            Account account;
            if (accountType.equalsIgnoreCase("Cheque")) {
                String employer = view.getTxtEmployer().getText();
                String employerAddress = view.getTxtEmployerAddress().getText();

                if (employer == null || employer.trim().isEmpty()) {
                    showError("Employer name is required for Cheque accounts");
                    return;
                }

                if (employerAddress == null || employerAddress.trim().isEmpty()) {
                    showError("Employer address is required for Cheque accounts");
                    return;
                }

                if (!customer.canOpenChequeAccount()) {
                    showError("Customer must be employed to open a Cheque account");
                    return;
                }

                account = bank.openAccount(accountType, customer, initialDeposit,
                        branch, employer.trim(), employerAddress.trim());
            } else {
                account = bank.openAccount(accountType, customer, initialDeposit, branch.trim());
            }

            view.clearForm();
            showSuccess(String.format(
                    "Account opened successfully!%n%nAccount Number: %s%nAccount Type: %s%nInitial Balance: BWP %.2f",
                    account.getAccountNumber(),
                    account.getAccountType(),
                    account.getBalance()
            ));

        } catch (NumberFormatException ex) {
            showError("Invalid deposit amount. Please enter a valid number.");
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