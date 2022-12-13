package org.example;

import org.example.application.demo.DemoApp;
import org.example.application.game.UserApp;
import org.example.application.housing.HousingApp;
import org.example.application.socialmedia.SocialMediaApp;
import org.example.application.waiting.WaitingApp;
import org.example.server.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(new DemoApp());
        try {
            server.start(); // enter
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}