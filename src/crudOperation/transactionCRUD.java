package crudOperation;

import connection.DBConnection;
import models.TransactionModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class transactionCRUD {

    private Connection connection;

    public transactionCRUD() {
        this.connection = DBConnection.get_connection();
    }

    public List<TransactionModel> findAll() {
        String sql = "SELECT * FROM transaction;";
        List<TransactionModel> resultList = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            while (result.next()) {
                resultList.add(new TransactionModel(
                        result.getInt("id_transaction"),
                        result.getString("transaction_description"),
                        result.getInt("account_sending"),
                        result.getDouble("debit"),
                        result.getString("transaction_date")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }

}
