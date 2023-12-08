package DAO;

import connection.dbConnection;
import models.transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class transactionDAO implements crudOperator<transaction> {

    private Connection connection;

    public transactionDAO() {
        this.connection = dbConnection.get_connection();
    }

    @Override
    public List<transaction> findAll() {
        String sql = "SELECT * FROM transaction;";
        List<transaction> resultList = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            while (result.next()) {
                resultList.add(new transaction(
                        result.getInt("id"),
                        result.getString("label"),
                        result.getDouble("montant"),
                        result.getTimestamp("date_transaction"),
                        result.getString("type_transaction"),
                        result.getInt("compte_id")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }
}
