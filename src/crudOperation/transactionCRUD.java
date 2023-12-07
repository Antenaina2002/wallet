package crudOperation;

import connection.dbConnection;
import models.transactionModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class transactionCRUD {

    private Connection connection;

    public transactionCRUD() {
        this.connection = dbConnection.get_connection();
    }

    public List<transactionModel> findAll() {
        String sql = "SELECT * FROM transaction;";
        List<TransactionModel> resultList = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            while (result.next()) {
                resultList.add(new transactionModel(
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
