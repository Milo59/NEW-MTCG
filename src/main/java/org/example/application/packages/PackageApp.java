package org.example.application.packages;

import org.example.application.packages.controller.PackageController;
import org.example.application.packages.repository.PackageDbRepository;
import org.example.application.packages.repository.PackageRepository;
import org.example.application.sessions.model.MemorySession;
import org.example.application.sessions.repository.SessionMemoryRepository;
import org.example.server.Application;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.http.ContentType;
import org.example.server.http.StatusCode;

public class PackageApp implements Application {

    private PackageController packageController;

    public PackageApp() {
        PackageRepository packageRepository = new PackageDbRepository();
        this.packageController = new PackageController(packageRepository);
    }

    @Override
    public Response handle(Request request) {
        //token verification
        String token = request.getToken();
        if (null == token || null == MemorySession.get(token) || !MemorySession.get(token).getUsername().equals("admin")){
            Response response = new Response();
            response.setStatusCode(StatusCode.NOT_FOUND);
            response.setContentType(ContentType.TEXT_PLAIN);
            response.setContent("authentication failed");
            return response;
        }else{
            return this.packageController.handle(request);
        }
    }
}
