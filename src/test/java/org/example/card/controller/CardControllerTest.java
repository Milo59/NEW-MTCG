package org.example.card.controller;

import org.example.application.card.CardApp;
import org.example.application.sessions.model.MemorySession;
import org.example.application.user.model.User;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.junit.jupiter.api.Test;

public class CardControllerTest {
    CardApp cardApp ;

    void mockLogin(){
        User user = new User();
        user.setUsername("kienboec");
        user.setId(44l);
        MemorySession.put("kienboec-mtcgToken",user);
    }

    @Test
    void testGetUserCard() throws Exception {
        //mock login
        mockLogin();
        cardApp = new CardApp();

        Request request = new Request();
        request.setMethod("GET");
        request.setPath("/cards");
        request.setToken("kienboec-mtcgToken");

        // Act
        Response response = cardApp.handle(request);

        //Print test output
        System.out.println(response.getStatus());
        System.out.println(response.getContent());
    }

    @Test
    void testGetUserCardFail() throws Exception {
        //mock login
        mockLogin();
        cardApp = new CardApp();

        Request request = new Request();
        request.setMethod("GET");
        request.setPath("/cards");
        //request.setToken("kienboec-mtcgToken");

        // Act
        Response response = cardApp.handle(request);

        //Print test output
        System.out.println(response.getStatus());
        System.out.println(response.getContent());
    }
}
