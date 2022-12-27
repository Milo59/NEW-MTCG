package org.example.application.battles;

import org.example.application.battles.controller.BattlesController;
import org.example.application.battles.repository.BattlesDbRepository;
import org.example.application.battles.repository.BattlesRepository;
import org.example.application.deck.repository.DeckDbRepository;
import org.example.application.deck.repository.DeckRepository;
import org.example.application.sessions.model.MemorySession;
import org.example.application.user.repository.UserDbRepository;
import org.example.application.user.repository.UserRepository;
import org.example.server.Application;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.http.ContentType;
import org.example.server.http.StatusCode;

public class BattlesApp implements Application {

    private BattlesController battlesController;

    public BattlesApp() {
        BattlesRepository battlesRepository = new BattlesDbRepository();
        DeckRepository deckRepository = new DeckDbRepository();
        UserRepository userRepository = new UserDbRepository();
        this.battlesController = new BattlesController(battlesRepository,deckRepository,userRepository);
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
            return this.battlesController.handle(request);
        }
    }
}
