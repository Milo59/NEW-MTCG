package org.example.application.card.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.application.card.model.Card;
import org.example.application.card.repository.CardRepository;
import org.example.application.sessions.model.MemorySession;
import org.example.application.user.model.User;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.http.ContentType;
import org.example.server.http.Method;
import org.example.server.http.StatusCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardController {

    private final CardRepository cardRepository;

    public CardController(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Response handle(Request request) {
        String method = request.getMethod();
        if (method.equals(Method.GET.method)) {
            return searchUserCard(request);
        }
        Response response = new Response();
        response.setStatusCode(StatusCode.METHODE_NOT_ALLOWED);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setContent(request.getMethod() + ": Not allowed for " + request.getPath());

        return response;
    }

    private Response searchUserCard(Request request) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Card> cardList;

        Response response = new Response();
        response.setStatusCode(StatusCode.OK);
        response.setContentType(ContentType.APPLICATION_JSON);
        try {

            User user = MemorySession.get(request.getToken());// return a User object with all info

            cardList = cardRepository.findCardByUserId(user.getId());
            String content = "";

            content = objectMapper.writeValueAsString(cardList);
            response.setContent(content);

        } catch (Exception e) {
            e.printStackTrace();
            response.setContent(e.getMessage());
        }
        return response;
    }
}
