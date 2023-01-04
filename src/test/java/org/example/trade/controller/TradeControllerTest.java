package org.example.trade.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.application.sessions.model.MemorySession;
import org.example.application.trade.TradeApp;
import org.example.application.trade.model.Trade;
import org.example.application.transactions.TransactionsApp;
import org.example.application.user.model.User;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.junit.jupiter.api.Test;

public class TradeControllerTest {

    TradeApp tradeApp;

    void mockLogin() {
        User user = new User();
        user.setUsername("kienboec");
        user.setId(44L); //L --> Long
        MemorySession.put("kienboec-mtcgToken", user);
    }

    @Test
    void testCheckTrading() throws Exception{
        //mock login
        mockLogin();

        tradeApp = new TradeApp();

        Request request = new Request();
        request.setMethod("GET");
        request.setPath("/tradings");

        request.setToken("kienboec-mtcgToken");

        // Act
        Response response = tradeApp.handle(request);

        //Print test output
        System.out.println(response.getStatus());
        System.out.println(response.getContent());
    }

    @Test
    void testCreateTrading() throws Exception{
        //mock login
        mockLogin();

        tradeApp = new TradeApp();

        Request request = new Request();
        request.setMethod("POST");
        request.setPath("/tradings");

        request.setToken("kienboec-mtcgToken");
        Trade trade = new Trade();
        trade.setId("6cd85277-4590-49d4-b0cf-ba0a921faad0");
        trade.setCardToTrade("1cb6ab86-bdb2-47e5-b6e4-68c5ab389334");
        trade.setType("monster");
        trade.setMinimumDamage(15);

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(trade);
        request.setContent(content);


        // Act
        Response response = tradeApp.handle(request);

        //Print test output
        System.out.println(response.getStatus());
        System.out.println(response.getContent());
    }

    @Test
    void testTryToTrade() throws Exception{
        //mock login
        mockLogin();

        tradeApp = new TradeApp();

        Request request = new Request();
        request.setMethod("POST");
        request.setPath("/tradings");

        request.setToken("kienboec-mtcgToken");
        Trade trade = new Trade();
        trade.setId("6cd85277-4590-49d4-b0cf-ba0a921faad0");
        trade.setCardToTrade("1cb6ab86-bdb2-47e5-b6e4-68c5ab389334");
        trade.setType("monster");
        trade.setMinimumDamage(15);

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(trade);
        request.setContent(content);


        // Act
        Response response = tradeApp.handle(request);

        //Print test output
        System.out.println(response.getStatus());
        System.out.println(response.getContent());
    }


}