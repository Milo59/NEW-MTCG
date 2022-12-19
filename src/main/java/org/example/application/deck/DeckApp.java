package org.example.application.deck;

import org.example.application.deck.controller.DeckController;
import org.example.application.deck.repository.DeckDbRepository;
import org.example.application.deck.repository.DeckRepository;
import org.example.application.packages.controller.PackageController;
import org.example.application.sessions.model.MemorySession;
import org.example.server.Application;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.http.ContentType;
import org.example.server.http.StatusCode;

public class DeckApp implements Application {

    private DeckController deckController;

    public DeckApp() {
        DeckRepository deckRepository = new DeckDbRepository();
        this.deckController = new DeckController(deckRepository);
    }

    @Override
    public Response handle(Request request) {
        //token verification
        String token = request.getToken();
        if (null == token || null == MemorySession.get(token)){
            Response response = new Response();
            response.setStatusCode(StatusCode.NOT_FOUND);
            response.setContentType(ContentType.TEXT_PLAIN);
            response.setContent("authentication failed");
            return response;
        }else{
            return this.deckController.handle(request);
        }
    }
}
