package org.example;

import org.example.application.GameApp;
import org.example.server.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(new GameApp());
        try {
            server.start(); // enter
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}