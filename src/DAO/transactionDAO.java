package DAO;

import connection.dbConnection;
import models.transaction;
import models.transaction;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class transactionDAO implements crudOperator<transaction> {

    private Connection connection;

    public transactionDAO() {
        this.connection = dbConnection.get_connection();
    }

    public class TransactionProcessor {

        public static Map<String, Double> calculateAmountByCategory(
                List<transaction> transactions,
                int accountId,
                Timestamp startDate,
                Timestamp endDate
        ) {
            Map<String, Double> amountByCategory = new HashMap<>();

            for (transaction t : transactions) {
                if (t.getAccountId() == accountId &&
                        t.getDate().after(startDate) &&
                        t.getDate().before(endDate)) {
                    String category = t.getType();
                    amountByCategory.put(category, amountByCategory.getOrDefault(category, 0.0) + t.getAmount());
                }
            }

            for (String category : getAllCategories(transactions, accountId)) {
                amountByCategory.putIfAbsent(category, 0.0);
            }

            return amountByCategory;
        }

        private static List<String> getAllCategories(List<transaction> transactions, int accountId) {
            return transactions.stream()
                    .filter(t -> t.getAccountId() == accountId)
                    .map(transaction::getType)
                    .distinct()
                    .collect(Collectors.toList());
        }
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
