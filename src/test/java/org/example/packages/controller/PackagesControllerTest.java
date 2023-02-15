package org.example.packages.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.application.card.model.Card;
import org.example.application.packages.PackageApp;
import org.example.application.sessions.model.MemorySession;
import org.example.application.user.model.User;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class PackagesControllerTest {

    PackageApp packageApp;

    void mockLogin() {
        User user = new User();
        user.setUsername("admin");
        MemorySession.put("admin-mtcgToken", user);
    }

    void mockLogin2() {
        User user = new User();
        user.setUsername("kienboec");
        MemorySession.put("kienboec-mtcgToken", user);
    }

    @Test
    void testCreatePackages() throws Exception {
        //mock login
        mockLogin();
        packageApp = new PackageApp();

        Request request = new Request();
        request.setMethod("POST");
        request.setPath("/packages");

        List<Card> cards = new ArrayList<>();
        Card card1 = new Card("845f0dc7-37d0-426e-994e-43fc3ac83c08", "WaterGoblin", 10.0f);
        Card card2 = new Card("99f8f8dc-e25e-4a95-aa2c-782823f36e2a", "Dragon", 50.0f);
        Card card3 = new Card("e85e3976-7c86-4d06-9a80-641c2019a79f", "WaterSpell", 20.0f);
        Card card4 = new Card("1cb6ab86-bdb2-47e5-b6e4-68c5ab389334", "Ork", 45.0f);
        Card card5 = new Card("dfdd758f-649c-40f9-ba3a-8657f4b3439f", "FireSpell", 25.0f);

        cards.add(card1);
        cards.add(card2);
        cards.add(card3);
        cards.add(card4);
        cards.add(card5);

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(cards);
        request.setContent(content);
        request.setToken("admin-mtcgToken");

        // Act
        Response response = packageApp.handle(request);

        //Print test output
        System.out.println(response.getStatus());
        System.out.println(response.getContent());
    }

    /*@Test
    void testBuyPackages() throws Exception {
        //mock login
        mockLogin2();
        packageApp = new PackageApp();

        Request request = new Request();
        request.setMethod("POST");
        request.setPath("/transactions/packages");

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(  request.setContent());
        request.setContent(content);
        request.setToken("kienboec-mtcgToken");

        // Act
        Response response = packageApp.handle(request);

        //Print test output
        System.out.println(response.getStatus());
        System.out.println(response.getContent());
    }*/
}
