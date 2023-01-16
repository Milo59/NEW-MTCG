package org.example.application.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.application.user.model.User;
import org.example.application.user.repository.UserRepository;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.http.ContentType;
import org.example.server.http.Method;
import org.example.server.http.StatusCode;

import java.util.HashMap;
import java.util.Map;

public class UserController {

    private final UserRepository userRepository;


    public UserController(UserRepository userRepository) { //构造函数形参
        this.userRepository = userRepository;
    }

    public Response handle(Request request) {
        String method = request.getMethod();
        String path = request.getPath();

        if (path.equals("/users") && method.equals(Method.POST.method)) {
            //Determine whether it is a POST request method, indicating that a user is to be created
            return create(request);
        }

        if (path.startsWith("/users/") && request.getMethod().equals(Method.GET.method)) { // 14 Get method go this way
            return find(request);
        }

        if (path.startsWith("/users/") && request.getMethod().equals(Method.PUT.method)) { // 14 Post Method come here
            return update(request);
        }

        Response response = new Response();
        response.setStatusCode(StatusCode.METHODE_NOT_ALLOWED);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setContent(request.getMethod() + ": Not allowed for " + request.getPath());

        return response;
    }

    //edit user data
    private Response update(Request request) {
        ObjectMapper objectMapper = new ObjectMapper();//实例化objectMapper对象 Instantiate the objectMapper object

        String username = request.getPath().replaceAll("/users/","");

        String json = request.getContent();
        User user;
        try {
            user = objectMapper.readValue(json, User.class);//将json转为对象 Turn JSON into objects
            user.setUsername(username);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        Response response = new Response();
        response.setStatusCode(StatusCode.OK);
        response.setContentType(ContentType.APPLICATION_JSON);
        try {
            userRepository.update(user);
            String content = "";
            Map map = new HashMap();
            map.put("msg","editing user data succeeded");
            map.put("user",userRepository.findByUsername(username));
            content = objectMapper.writeValueAsString(map);//将map转为json
            response.setContent(content);
        } catch (Exception e) {
            e.printStackTrace();
            response.setContent(e.getMessage());
        }
        return response;
    }

    private Response find(Request request) {
        ObjectMapper objectMapper = new ObjectMapper();
        String path = request.getPath();
        String username = path.replaceAll("/users/","");
        Response response = new Response();
        response.setStatusCode(StatusCode.OK);
        response.setContentType(ContentType.APPLICATION_JSON);
        try{
            User user = userRepository.findByUsername(username);
            String content = "";
            content = objectMapper.writeValueAsString(user);
            response.setContent("User information "+content);
        } catch (Exception e) {
            e.printStackTrace();
            response.setContent(e.getMessage());
        }
        return response;
    }


    private Response create(Request request) { //request getContent gets the parameters of the instrumentation script and encapsulates them in the user object
        ObjectMapper objectMapper = new ObjectMapper();

        String json = request.getContent();
        User user;
        try {
            user = objectMapper.readValue(json, User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        Response response = new Response();
        try {
            if (userRepository.save(user)){ //return true
                response.setStatusCode(StatusCode.CREATED);
                response.setContentType(ContentType.APPLICATION_JSON);
                String content = "";
                Map map = new HashMap();
                map.put("msg","created successfully");
                map.put("user",user);
                user.setPassword(user.getHashPassword()); //password hash
                content = objectMapper.writeValueAsString(map);
                response.setContent(content);
            }else{ //FALSE
                response.setStatusCode(StatusCode.OK);
                response.setContentType(ContentType.APPLICATION_JSON);
                response.setContent("username already exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode(StatusCode.OK);
            response.setContentType(ContentType.APPLICATION_JSON);
            response.setContent(e.getMessage());
        }
        return response;
    }

}
