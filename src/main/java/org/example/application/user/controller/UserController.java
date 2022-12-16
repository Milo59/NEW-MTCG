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
        if (method.equals(Method.POST.method)) {
            //判断post请求方法 表示要创建用户
            return create(request);
        }

        /*if (request.getMethod().equals(Method.GET.method)) {
            return readAll();
        }*/

        // To do...get方法

        Response response = new Response();
        response.setStatusCode(StatusCode.METHODE_NOT_ALLOWED);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setContent(request.getMethod() + ": Not allowed for " + request.getPath());

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
            if (userRepository.save(user)){ //根据传的参数调用方法
                response.setStatusCode(StatusCode.CREATED);
                response.setContentType(ContentType.APPLICATION_JSON);
                String content = "";
                Map map = new HashMap();
                map.put("msg","created successfully");
                map.put("user",user);
                content = objectMapper.writeValueAsString(map);
                response.setContent(content);
            }else{
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
