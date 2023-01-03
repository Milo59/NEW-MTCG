package org.example.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.application.user.UserApp;
import org.example.application.user.repository.UserDbRepository;
import org.example.application.user.repository.UserRepository;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserControllerTest {
    UserRepository userRepository = new UserDbRepository();
    UserApp userApp;
    @Test
    void testCreateUsers() throws Exception {
        userApp = new UserApp();

        Request request = new Request();
        request.setMethod("POST");
        request.setPath("/users");

        Map<String,String> map = new HashMap<>();
        map.put("Username","kienboec");
        map.put("Password","daniel");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(map);
        request.setContent(content);

        // Act
        Response response = userApp.handle(request);

        //Print test output
        System.out.println(response.getStatus());
        System.out.println(response.getContent());
    }
}
