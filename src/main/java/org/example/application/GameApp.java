package org.example.application;

import org.example.server.Application;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.http.ContentType;
import org.example.server.http.StatusCode;

public class GameApp implements Application {

    private String title = "Welcome to Monster Trading Cards Game!";

    /**
     * / root path handle
     * @param request
     * @return
     */
    public Response indexHandle(Request request){ //default from RequestHandler. Other Situation
        Response response = new Response();
        response.setStatusCode(StatusCode.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setContent(title);
        return response;
    }

    @Override
    public Response handle(Request request) {
        Response response = new Response();
        response.setStatusCode(StatusCode.NOT_FOUND);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setContent(request.getPath()+" "+StatusCode.NOT_FOUND.message);
        return response;
    }

}
