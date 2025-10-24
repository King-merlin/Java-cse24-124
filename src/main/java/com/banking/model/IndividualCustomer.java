package com.banking.model;

public class IndividualCustomer extends Customer {
    private boolean employed;

    public IndividualCustomer(String customerId, String firstName,
                              String lastName, String address) {
        super(customerId, firstName, lastName, address);
        this.employed = false;
    }

    public IndividualCustomer(String customerId, String firstName,
                              String lastName, String address, boolean employed) {
        super(customerId, firstName, lastName, address);
        this.employed = employed;
    }

    @Override
    public boolean canOpenChequeAccount() {
        return employed;
    }

    public boolean isEmployed() {
        return employed;
    }

    public void setEmployed(boolean employed) {
        this.employed = employed;
    }
}
