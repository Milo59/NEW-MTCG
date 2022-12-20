package org.example.application.user;

import org.example.application.sessions.model.MemorySession;
import org.example.application.user.controller.UserController;
import org.example.application.user.repository.UserDbRepository;
import org.example.application.user.repository.UserRepository;
import org.example.server.Application;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.http.ContentType;
import org.example.server.http.Method;
import org.example.server.http.StatusCode;

public class UserApp implements Application {

    private UserDbRepository userMemoryRepository;
    private UserController userController;

    public UserApp() {
        UserRepository userRepository = new UserDbRepository();
        this.userController = new UserController(userRepository);
    }

    @Override
    public Response handle(Request request) {
        String path = request.getPath();
        if (path.replaceAll("/users","").length()>1){
            //token verification
            Response response = new Response();
            response.setStatusCode(StatusCode.NOT_FOUND);
            response.setContentType(ContentType.TEXT_PLAIN);

            String token = request.getToken();
            String username = path.replaceAll("/users/","");
            response.setContent("authentication failed");
            if (null == token || null == MemorySession.get(token)){
                return response;
            }
            if (!MemorySession.get(token).getUsername().equals(username)){
                if (request.getMethod().equals(Method.GET.method)){
                    response.setContent("you can only view your own information");
                }else if (request.getMethod().equals(Method.PUT.method)){
                    response.setContent("you can only update your own information");
                }
                return response;
            }
        }
        return userController.handle(request);
    }
}
