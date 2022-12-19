package org.example;

import org.example.application.GameApp;
import org.example.application.utils.DatabaseUtil;
import org.example.server.Server;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        Server server = new Server(new GameApp());
        try {
            server.start(); // enter
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            DatabaseUtil.getConnection().close();
        }
    }
}