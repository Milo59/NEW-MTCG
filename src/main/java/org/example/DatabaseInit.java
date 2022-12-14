package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseInit {

    private static String DB_URL = "jdbc:postgresql://lmusoft.top:5432/swe1db";
    private static String DB_USER = "swe1user";
    private static String DB_PW = "swe1pw";

    public static void main(String[] args) throws SQLException {
        Connection conn = DriverManager.getConnection(
                DB_URL, DB_USER, DB_PW
        );

        // Create user table
        Statement stmt1 = conn.createStatement();
        stmt1.execute(
        """
            CREATE TABLE IF NOT EXISTS users (
                username VARCHAR(255) PRIMARY KEY,
                password VARCHAR(255) NOT NULL
            );
            """
        );
        stmt1.close();

        // Use try-with-resources statement
        try (Statement stmt2 = conn.createStatement()) {
            stmt2.execute(
            """
                INSERT INTO users(username, password)
                VALUES('marvin', 'password');
                """
            );
        }

        String username = "john";
        String password = "123456";

        // DO NOT! SQL Injection vulnerability
        // password = "test'); DROP TABLE users; -- ";
        String wrongSql =
                "INSERT INTO users(username, password) VALUES('" + username + "', '" + password + "')";

        // Instead use Prepared Statements
        String preparedSql = "INSERT INTO users(username, password) VALUES(?, ?)";

        try(PreparedStatement ps = conn.prepareStatement(preparedSql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.execute();
        }

        // findAll example
        String userFindAllSql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try(
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(userFindAllSql)
                ) {
            while (rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                users.add(user);
            }
        }
        System.out.println("findAll results:");
        System.out.println(users);
        System.out.println();

        // findByUsername example
        String userFindByUsernameSql = "SELECT * FROM users WHERE username = ?";
        String findUsername = "john";
        User user = null;
        try(PreparedStatement ps = conn.prepareStatement(userFindByUsernameSql)) {
            ps.setString(1, findUsername);
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                }
            }
        }
        System.out.println("findByUsername results:");
        System.out.println(user);
        System.out.println();

        conn.close();
    }

    public static class User {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String toString() {
            return "User{" +
                    "username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }
}
