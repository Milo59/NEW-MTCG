package org.example.application.sessions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.application.sessions.model.MemorySession;
import org.example.application.sessions.repository.SessionRepository;
import org.example.application.user.model.User;
import org.example.application.user.repository.UserRepository;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.http.ContentType;
import org.example.server.http.Method;
import org.example.server.http.StatusCode;

import java.util.HashMap;
import java.util.Map;

public class SessionController {
    private final SessionRepository sessionRepository;// final 一旦你将引用声明作final，你将不能改变这个引用了
    private final UserRepository userRepository;

    public SessionController(SessionRepository sessionRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    public Response handle(Request request) {
        String method = request.getMethod();
        String path = request.getPath();
        if (method.equals(Method.POST.method)) {
            if (path.equals("/sessions")) {
                return login(request);
            } else if (path.equals("/sessions/logout")) { //additional feature
                return logout(request);
            }
        }

        Response response = new Response();
        response.setStatusCode(StatusCode.METHODE_NOT_ALLOWED);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setContent(request.getMethod() + ": Not allowed for " + request.getPath());

        return response;
    }

    private Response login(Request request) {
        ObjectMapper objectMapper = new ObjectMapper();

        String json = request.getContent();
        User user;

        Response response = new Response();
        response.setStatusCode(StatusCode.OK);
        response.setContentType(ContentType.APPLICATION_JSON);
        try {
            user = objectMapper.readValue(json, User.class);
        } catch (Exception e) {
            e.printStackTrace();
            response.setContent(e.getMessage());
            return response;
        }

        try {
            if (sessionRepository.login(user)) { //from SessionMemoryRepository return TRUE
                String content = "";
                Map map = new HashMap();
                map.put("msg", "login succeeded");
                String authorization = user.getUsername() + "-mtcgToken";
                map.put("authorization", authorization);
                MemorySession.put(authorization, userRepository.findByUsername(user.getUsername()));

                //print current number of people online
                System.out.println(user.getUsername() + " login succeeded");
                System.out.println("number of people online: " + MemorySession.online());


                content = objectMapper.writeValueAsString(map);
                response.setContent(content);
            } else {  //
                System.out.println(user.getUsername() + " login failed,username or password error");
                response.setContent("username or password error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode(StatusCode.OK);
            response.setContentType(ContentType.APPLICATION_JSON);
            response.setContent(e.getMessage());
        }
        return response;
    }

    private Response logout(Request request) {
        Response response = new Response();
        response.setStatusCode(StatusCode.OK);
        response.setContentType(ContentType.APPLICATION_JSON);
        MemorySession.remove(request.getToken());
        response.setContent("logout succeeded");
        return response;
    }

}
