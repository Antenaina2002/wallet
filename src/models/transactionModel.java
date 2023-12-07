package models;

import java.sql.Timestamp;

public class transactionModel {
    private int id;
    private String type;
    private int accountId;
    private double amount;
    private Timestamp date;

    public transactionModel(int idTransaction, String transactionDescription, int accountSending, double debit, Timestamp transactionDate) {
        this.id = idTransaction;
        this.type = transactionDescription;
        this.accountId = accountSending;
        this.amount = debit;
        this.date = transactionDate;
    }

    // Getters and setters for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
