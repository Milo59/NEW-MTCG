package org.example.application.packages.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.application.card.model.Card;
import org.example.application.packages.model.Packages;
import org.example.application.packages.repository.PackageRepository;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.http.ContentType;
import org.example.server.http.Method;
import org.example.server.http.StatusCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackageController {
    private final PackageRepository packageRepository;

    public PackageController(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    public Response handle(Request request) {
        String method = request.getMethod();
        if (method.equals(Method.POST.method)) {
            return create(request);
        }
        Response response = new Response();
        response.setStatusCode(StatusCode.METHODE_NOT_ALLOWED);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setContent(request.getMethod() + ": Not allowed for " + request.getPath());

        return response;
    }

    private Response create(Request request) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = request.getContent();
        List<Card> packageList;

        Response response = new Response();
        response.setStatusCode(StatusCode.OK);
        response.setContentType(ContentType.APPLICATION_JSON);
        try {
            packageList = objectMapper.readValue(json, new TypeReference<List<Card>>() {});

            if (packageRepository.save(packageList)){
                response.setStatusCode(StatusCode.CREATED);
                String content = "";
                Map map = new HashMap();
                map.put("msg","created successfully");
                map.put("package",packageList);
                content = objectMapper.writeValueAsString(map);
                response.setContent(content);
            }else{
                response.setContentType(ContentType.APPLICATION_JSON);
                response.setContent("package already exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setContent(e.getMessage());
        }
        return response;
    }
}