package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConnection {
    private static Connection connection;

    public static Connection get_connection(){
        if (connection == null){
            try {
                connection = DriverManager.getConnection(connectionParams.URL, connectionParams.USER, connectionParams.PASSWORD);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }
}
