package org.example.application.stats.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.application.sessions.model.MemorySession;
import org.example.application.stats.model.Stats;
import org.example.application.user.model.User;
import org.example.application.user.repository.UserRepository;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.http.ContentType;
import org.example.server.http.Method;
import org.example.server.http.StatusCode;

import java.util.HashMap;
import java.util.Map;

public class StatsController {

    private final UserRepository userRepository;

    public StatsController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Response handle(Request request) {
        String method = request.getMethod();
        if (method.equals(Method.GET.method)) {
            return getUserStat(request);
        }
        Response response = new Response();
        response.setStatusCode(StatusCode.METHODE_NOT_ALLOWED);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setContent(request.getMethod() + ": Not allowed for " + request.getPath());

        return response;
    }

    private Response getUserStat(Request request) {
        ObjectMapper objectMapper = new ObjectMapper();
        Response response = new Response();
        response.setStatusCode(StatusCode.OK);
        response.setContentType(ContentType.APPLICATION_JSON);
        try {
            String token = request.getToken();
            User user = MemorySession.get(token);
            Stats stats = userRepository.rank(user);
            Map<String,Object> map = new HashMap<>();
            map.put("username",stats.getUsername());
            map.put("name",stats.getName());
            map.put("money",stats.getMoney());
            map.put("bio",stats.getBio());
            map.put("image",stats.getImage());
            map.put("score",stats.getScored());
            String content = "";
            content = objectMapper.writeValueAsString(map);
            response.setContent(content);
        } catch (Exception e) {
            e.printStackTrace();
            response.setContent(e.getMessage());
        }
        return response;
    }


}
