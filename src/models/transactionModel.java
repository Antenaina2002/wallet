package models;

public class transactionModel {
    private int id;
    private String type;
    private int accountId;
    private double amount;
    private String date;

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
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
