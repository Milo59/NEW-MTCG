package org.example.transactions.controller;

import org.example.application.sessions.model.MemorySession;
import org.example.application.transactions.TransactionsApp;
import org.example.application.user.model.User;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.junit.jupiter.api.Test;

public class TransactionsControllerTest {
    TransactionsApp transactionsApp ;

    void mockLogin(){
        User user = new User();
        user.setUsername("kienboec");
        MemorySession.put("kienboec-mtcgToken",user);
    }

    @Test
    void testAcquirePackages() throws Exception{
        //mock login
        mockLogin();

        transactionsApp = new TransactionsApp();

        Request request = new Request();
        request.setMethod("POST");
        request.setPath("/transactions/packages");

        request.setToken("kienboec-mtcgToken");

        // Act
        Response response = transactionsApp.handle(request);

        //Print test output
        System.out.println(response.getStatus());
        System.out.println(response.getContent());
    }
}
