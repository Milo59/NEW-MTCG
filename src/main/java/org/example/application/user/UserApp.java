package org.example.application.user;

import org.example.application.user.controller.UserController;
import org.example.application.user.repository.UserDbRepository;
import org.example.application.user.repository.UserRepository;
import org.example.server.Application;
import org.example.server.dto.Request;
import org.example.server.dto.Response;

public class UserApp implements Application {

    private UserDbRepository userMemoryRepository;
    private UserController userController;

    public UserApp() {
        UserRepository userRepository = new UserDbRepository();
        this.userController = new UserController(userRepository);
    }

    @Override
    public Response handle(Request request) {
        return userController.handle(request);
    }
}
