package org.example.application.housing.controller;

import org.example.application.housing.Repository.HouseRepository;
import org.example.application.housing.model.House;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.http.ContentType;
import org.example.server.http.StatusCode;

import java.util.List;

public class HouseController {

    private final HouseRepository houseRepository;

    public HouseController(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    public Response handle(Request request) {

        if (request.getMethod().equals("GET")) {
            return getHouses(request);
        }

        Response response = new Response();
        response.setStatusCode(StatusCode.METHODE_NOT_ALLOWED);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setContent(request.getMethod() + ": Not allowed for " + request.getPath());

        return response;
    }

    private Response getHouses(Request request) {
        List<House> houses = this.houseRepository.findAll();

        Response response = new Response();
        response.setStatusCode(StatusCode.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setContent(houses.toString());

        return response;
    }
}
