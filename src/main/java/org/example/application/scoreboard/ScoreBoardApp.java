package org.example.application.scoreboard;

import org.example.application.scoreboard.controller.ScoreBoardController;
import org.example.application.sessions.model.MemorySession;
import org.example.application.user.repository.UserDbRepository;
import org.example.application.user.repository.UserRepository;
import org.example.server.Application;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.http.ContentType;
import org.example.server.http.StatusCode;

public class ScoreBoardApp implements Application {
    private ScoreBoardController scoreBoardController;

    public ScoreBoardApp() {
        UserRepository userRepository = new UserDbRepository();
        this.scoreBoardController = new ScoreBoardController(userRepository);
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
            return this.scoreBoardController.handle(request);
        }
    }
}
