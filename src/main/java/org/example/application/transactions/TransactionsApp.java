package org.example.application.transactions;

import org.example.application.packages.controller.PackageController;
import org.example.application.packages.repository.PackageDbRepository;
import org.example.application.packages.repository.PackageRepository;
import org.example.application.sessions.model.MemorySession;
import org.example.application.user.repository.UserDbRepository;
import org.example.application.user.repository.UserRepository;
import org.example.server.Application;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.http.ContentType;
import org.example.server.http.StatusCode;

public class TransactionsApp implements Application {

    private PackageController packageController;

    public TransactionsApp() {
        PackageRepository packageRepository = new PackageDbRepository();
        UserRepository userRepository = new UserDbRepository();
        this.packageController = new PackageController(packageRepository,userRepository);
    }

    @Override
    public Response handle(Request request) {
        //token verification
        String token = request.getToken();
        if (null == token || null == MemorySession.get(token)){
            Response response = new Response();
            response.setStatusCode(StatusCode.OK);
            response.setContentType(ContentType.TEXT_PLAIN);
            response.setContent("authentication failed");
            return response;
        }else{
            return this.packageController.handle(request);
        }
    }
}
