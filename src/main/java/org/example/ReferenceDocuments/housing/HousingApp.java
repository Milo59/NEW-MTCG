package org.example.ReferenceDocuments.housing;

import org.example.ReferenceDocuments.housing.Repository.HouseMemoryRepository;
import org.example.ReferenceDocuments.housing.controller.HouseController;
import org.example.server.Application;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.http.ContentType;
import org.example.server.http.StatusCode;

public class HousingApp implements Application {

    private HouseMemoryRepository houseMemoryRepository;
    private HouseController houseController;

    public HousingApp() {
        this.inject();
    }

    @Override
    public Response handle(Request request) {

        if (request.getPath().equals("/houses")) {
            return houseController.handle(request);
        }

        Response response = new Response();
        response.setStatusCode(StatusCode.NOT_FOUND);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setContent(request.getPath() + ": Not Found");

        return response;
    }

    private void inject() {
        this.houseMemoryRepository = new HouseMemoryRepository();
        this.houseController = new HouseController(this.houseMemoryRepository);
    }
}
