package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseUtil {
    //get the connection to the database
    private static String DB_URL = "jdbc:postgresql://localhost:5431/swe1db";
    private static String DB_USER = "swe1user";
    private static String DB_PW = "swe1pw";

    private static Connection connection;

    private DatabaseUtil() {
    }

    static {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    DB_URL, DB_USER, DB_PW
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    // return connection
    public static Connection getConnection() {
        try {
            if (connection.isClosed()){
                connection = DriverManager.getConnection(
                        DB_URL, DB_USER, DB_PW
                );
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return connection;
    }

}
