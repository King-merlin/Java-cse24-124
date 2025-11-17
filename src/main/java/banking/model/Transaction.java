package banking.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String type; // DEPOSIT, WITHDRAW, INTEREST, etc.
    private double amount;
    private String date;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getDate() { return date; }

    @Override
    public String toString() {
        return String.format("[%s] %s: %.2f", date, type, amount);
    }
}
