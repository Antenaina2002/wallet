package com.example.walletspring.model;

import java.sql.Timestamp;

public class account {
    private int id;
    private String name;
    private double balanceAmount;
    private Timestamp balanceLastUpdate;
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public Timestamp getBalanceLastUpdate() {
        return balanceLastUpdate;
    }

    public void setBalanceLastUpdate(Timestamp balanceLastUpdate) {
        this.balanceLastUpdate = balanceLastUpdate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public account(int id, String name, double balanceAmount, Timestamp balanceLastUpdate, String type) {
        this.id = id;
        this.name = name;
        this.balanceAmount = balanceAmount;
        this.balanceLastUpdate = balanceLastUpdate;
        this.type = type;
    }
}
