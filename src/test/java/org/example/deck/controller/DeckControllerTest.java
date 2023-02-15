package org.example.deck.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.application.deck.DeckApp;
import org.example.application.sessions.model.MemorySession;
import org.example.application.user.model.User;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class DeckControllerTest {
    DeckApp deckApp;

    void mockLogin() {
        User user = new User();
        user.setUsername("kienboec");
        user.setId(44l); //l --> Long
        MemorySession.put("kienboec-mtcgToken", user);
    }

    @Test
    void testShowUserDeck() throws Exception {
        mockLogin();
        deckApp = new DeckApp();

        Request request = new Request();
        request.setMethod("GET");
        request.setPath("/deck");
        request.setToken("kienboec-mtcgToken");

        // Act
        Response response = deckApp.handle(request);

        //Print test output
        System.out.println(response.getStatus());
        System.out.println(response.getContent());
    }

    @Test
    void testShowUserDeckFormat() throws Exception {
        mockLogin();
        deckApp = new DeckApp();

        Request request = new Request();
        request.setMethod("GET");
        request.setPath("/deck?format=plain");
        request.setToken("kienboec-mtcgToken");

        // Act
        Response response = deckApp.handle(request);

        //Print test output
        System.out.println(response.getStatus());
        System.out.println(response.getContent());
    }

    @Test
    void testConfigureDeck() throws Exception {
        mockLogin();
        deckApp = new DeckApp();

        Request request = new Request();
        request.setMethod("PUT");
        request.setPath("/deck");
        request.setToken("kienboec-mtcgToken");

        List<String> cards = Arrays.asList("845f0dc7-37d0-426e-994e-43fc3ac83c08", "99f8f8dc-e25e-4a95-aa2c-782823f36e2a", "e85e3976-7c86-4d06-9a80-641c2019a79f", "171f6076-4eb5-4a7d-b3f2-2d650cc3d237");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(cards);
        request.setContent(content);
        // Act
        Response response = deckApp.handle(request);

        //Print test output
        System.out.println(response.getStatus());
        System.out.println(response.getContent());
    }

    @Test
    void testConfigureDeckFail() throws Exception { //only 3 cards set
        mockLogin();
        deckApp = new DeckApp();

        Request request = new Request();
        request.setMethod("PUT");
        request.setPath("/deck");
        request.setToken("kienboec-mtcgToken");

        List<String> cards = Arrays.asList("845f0dc7-37d0-426e-994e-43fc3ac83c08", "99f8f8dc-e25e-4a95-aa2c-782823f36e2a", "e85e3976-7c86-4d06-9a80-641c2019a79f");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(cards);
        request.setContent(content);
        // Act
        Response response = deckApp.handle(request);

        //Print test output
        System.out.println(response.getStatus());
        System.out.println(response.getContent());
    }
}
