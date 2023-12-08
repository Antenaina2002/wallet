package models;

import java.sql.Timestamp;

public class transaction {
    private int id;
    private String label;
    private double amount;
    private Timestamp date;
    private String type;
    private int accountId;

    public transaction(int id, String label, double amount, Timestamp date, String type, int accountId) {
        this.id = id;
        this.label = label;
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.accountId = accountId;
    }

    // Getters and setters for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getters and setters for label
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    // Getters and setters for amount
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    // Getters and setters for date
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    // Getters and setters for type
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Getters and setters for accountId
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
