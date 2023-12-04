package service;

import crudOperation.transactionCRUD;
import models.TransactionModel;

import java.util.List;

public class transactionService {
    private final TransactionCRUD transactionCRUD;

    public transactionService() {
        this.transactionCRUD = new TransactionCRUD();
    }

    public List<TransactionModel> getAllTransactions() {
        return transactionCRUD.findAll();
    }

    public TransactionModel saveTransaction(TransactionModel transaction) {
        return transactionCRUD.save(transaction);
    }

    public void updateTransaction(TransactionModel transaction) {
        transactionCRUD.update(transaction);
    }
