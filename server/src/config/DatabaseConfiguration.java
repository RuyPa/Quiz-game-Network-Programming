package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfiguration {
    public static String url = "jdbc:mysql://localhost:3306/btlltm?autoReconnect=true&useSSL=false";
    public static String username = "root";
    public static String password = "1234567";
    public static Connection connection;

    public DatabaseConfiguration(){
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
