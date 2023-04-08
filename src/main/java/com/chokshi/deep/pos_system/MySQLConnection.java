package com.chokshi.deep.pos_system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {

    static Connection connection;

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://192.168.0.151:3306/pos?userSSL=false";
        String username = "deep";
        String password = "deep@123";

        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
        return connection;
    }
}


