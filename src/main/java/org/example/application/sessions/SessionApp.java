package org.example.application.sessions;

import org.example.application.sessions.controller.SessionController;
import org.example.application.sessions.repository.SessionMemoryRepository;
import org.example.application.sessions.repository.SessionRepository;
import org.example.application.user.repository.UserDbRepository;
import org.example.application.user.repository.UserRepository;
import org.example.server.Application;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
//User Login
public class SessionApp implements Application {
    private final SessionController sessionController;

    public SessionApp() {
        SessionRepository sessionRepository = new SessionMemoryRepository();
        UserRepository userRepository = new UserDbRepository();
        this.sessionController = new SessionController(sessionRepository,userRepository);
    }

    @Override
    public Response handle(Request request) {
        return this.sessionController.handle(request);
    }
}
