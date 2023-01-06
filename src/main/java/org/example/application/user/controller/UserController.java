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

        if (path.startsWith("/users/") && request.getMethod().equals(Method.GET.method)) { // Get method go this way
            return find(request);
        }

        if (path.startsWith("/users/") && request.getMethod().equals(Method.PUT.method)) { // Post Method come here
            return update(request);
        }

        // To do...get method

        Response response = new Response();
        response.setStatusCode(StatusCode.METHODE_NOT_ALLOWED);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setContent(request.getMethod() + ": Not allowed for " + request.getPath());

        return response;
    }

    //edit user data
    private Response update(Request request) {
        ObjectMapper objectMapper = new ObjectMapper();

        String username = request.getPath().replaceAll("/users/","");

        String json = request.getContent();
        User user;
        try {
            user = objectMapper.readValue(json, User.class);
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
            content = objectMapper.writeValueAsString(map);
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


    private Response create(Request request) { //request getContent获取检测脚本的参数 封装到user对象里
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
            if (userRepository.save(user)){ //根据传的参数调用方法 往下走 如果返回值为TRUE
                response.setStatusCode(StatusCode.CREATED);
                response.setContentType(ContentType.APPLICATION_JSON);
                String content = "";
                Map map = new HashMap();
                map.put("msg","created successfully");
                map.put("user",user);
                user.setPassword(user.getHashPassword()); //密码 加密
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
