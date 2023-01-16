package org.example.application.deck.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.application.card.model.Card;
import org.example.application.deck.repository.DeckRepository;
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

public class DeckController {
    private final DeckRepository deckRepository;

    public DeckController(DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    public Response handle(Request request) {
        String method = request.getMethod();
        if (method.equals(Method.GET.method)) {
            return findDeckCard(request);
        }
        if (method.equals(Method.PUT.method)){
            return configureDeck(request);
        }
        Response response = new Response();
        response.setStatusCode(StatusCode.METHODE_NOT_ALLOWED);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setContent(request.getMethod() + ": Not allowed for " + request.getPath());

        return response;
    }

    private Response findDeckCard(Request request){
        ObjectMapper objectMapper = new ObjectMapper();
        Response response = new Response();
        response.setStatusCode(StatusCode.OK);
        response.setContentType(ContentType.APPLICATION_JSON);
        try {
            String token = request.getToken();
            User user = MemorySession.get(token);
            if(request.getPath().endsWith("?format=plain")){ //determine the path
                objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // format the output. To enable pretty print globally.
                //objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cardList); To enable pretty print on demand.
            }
            List<Card> cardList = deckRepository.findDeckCard(user);
            String content = "";

            content = objectMapper.writeValueAsString(cardList);
            response.setContent("This is "+user.getUsername()+"'s card on the deck "+content);
        } catch (Exception e) {
            e.printStackTrace();
            response.setContent(e.getMessage());
        }
        return response;
    }

    private Response configureDeck(Request request){
        ObjectMapper objectMapper = new ObjectMapper();
        String json = request.getContent();
        List<String> cardList;
        Response response = new Response();
        response.setStatusCode(StatusCode.OK);
        response.setContentType(ContentType.APPLICATION_JSON);
        try {
            String token = request.getToken();
            User user = MemorySession.get(token);
            cardList = objectMapper.readValue(json, new TypeReference<List<String>>() {});

            List<Card> deckCardList = deckRepository.findDeckCard(user);
            if(deckCardList.size()>0){  //4 cards a time
                String content = "";
                Map map = new HashMap();
                map.put("msg","Failed to configure. The card on the deck has been configured");
                map.put("cards",deckCardList);
                content = objectMapper.writeValueAsString(map);
                response.setContent(content);
                return response;
            }

            if(cardList.size()!=4){  //When not configured
                String content = "";
                Map map = new HashMap();
                map.put("msg","Failed to configure. The number of cards is not equal to 4"); // not enough
                map.put("cards",cardList);
                content = objectMapper.writeValueAsString(map);
                response.setContent(content);
                return response;
            }

            deckRepository.configureDeck(user,cardList);
            String content = "";
            Map map = new HashMap();
            map.put("msg","configure successfully");
            map.put("cards",cardList);
            content = objectMapper.writeValueAsString(map);
            response.setContent(content);
        } catch (Exception e) {
            e.printStackTrace();
            response.setContent(e.getMessage());
        }
        return response;
    }

}
