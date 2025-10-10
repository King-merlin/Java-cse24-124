package com.banking.model;

import java.util.Date;

public class Transaction {
    private double amount;
    private String type;
    private Date date;

    public Transaction(double amount, String type) {
        this.amount = amount;
        this.type = type;
        this.date = new Date();
    }

    public double getAmount() { return amount; }
    public String getType() { return type; }
    public Date getDate() { return date; }

    public void setAmount(double amount) { this.amount = amount; }
    public void setType(String type) { this.type = type; }
    public void setDate(Date date) { this.date = date; }
}