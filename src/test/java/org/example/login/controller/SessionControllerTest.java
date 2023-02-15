package org.example.login.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.application.sessions.SessionApp;
import org.example.application.sessions.repository.SessionMemoryRepository;
import org.example.application.sessions.repository.SessionRepository;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class SessionControllerTest {
    SessionRepository sessionRepository = new SessionMemoryRepository();
    SessionApp sessionApp;

    @Test
    void testLoginUsers() throws Exception {
        sessionApp = new SessionApp();

        Request request = new Request();
        request.setMethod("POST");
        request.setPath("/sessions");

        Map<String,String> map = new HashMap<>();
        map.put("Username","kienboec");
        map.put("Password","daniel");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(map);
        request.setContent(content);

        // Act
        Response response = sessionApp.handle(request);

        //Print test output
        System.out.println(response.getStatus());
        System.out.println(response.getContent());
    }
}
