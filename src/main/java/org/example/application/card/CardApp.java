package org.example.application.card;

import org.example.application.card.controller.CardController;
import org.example.application.card.repository.CardDBRepository;
import org.example.application.card.repository.CardRepository;
import org.example.application.sessions.model.MemorySession;
import org.example.server.Application;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.http.ContentType;
import org.example.server.http.StatusCode;

public class CardApp implements Application {
    private CardController cardController;

    public CardApp() {
       CardRepository cardRepository = new CardDBRepository();
       this.cardController = new CardController(cardRepository);
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
            return this.cardController.handle(request);
        }
    }
}
