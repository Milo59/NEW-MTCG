package org.example.application.trade.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.application.card.model.Card;
import org.example.application.sessions.model.MemorySession;
import org.example.application.trade.model.Trade;
import org.example.application.trade.repository.TradeRepository;
import org.example.application.user.model.User;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.http.ContentType;
import org.example.server.http.Method;
import org.example.server.http.StatusCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradeController {

   private final TradeRepository tradeRepository;


    public TradeController(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    public Response handle(Request request) {
        String method = request.getMethod();
        String path = request.getPath();
// 20 and 21
        if(method.equals(Method.GET.method ) && path.equals("/tradings")){
            return searchUserTrade(request);
        }

        if (method.equals(Method.POST.method ) && path.equals("/tradings")){
            return createTrade(request);
        }

        if (method.equals(Method.DELETE.method ) && path.equals("/tradings/6cd85277-4590-49d4-b0cf-ba0a921faad0")){
            return deleteTrade(request);
        }

        if (method.equals(Method.POST.method ) && path.startsWith("/tradings/")){//equals("/tradings/6cd85277-4590-49d4-b0cf-ba0a921faad0")){
            return tryToTrade(request);
        }

        Response response = new Response();
        response.setStatusCode(StatusCode.METHODE_NOT_ALLOWED);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setContent(request.getMethod() + ": Not allowed for " + request.getPath());

        return response;
    }

    private Response deleteTrade(Request request) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = request.getContent();
        Trade trade;//def null

        Response response = new Response();
        response.setStatusCode(StatusCode.OK);
        response.setContentType(ContentType.APPLICATION_JSON);

        try {
            trade = new Trade(); //??????????????????
            trade.setId(request.getPath().replace("/tradings/", "")); //???????????????id Intercept the id from the path
            // trade = objectMapper.readValue(json, Trade.class);
            User user = MemorySession.get(request.getToken());
            trade.setuId(user.getId());

            if (tradeRepository.delete(trade)) { // delete --> TradeDbRepository
                String content = "";
                Map map = new HashMap();
                map.put("msg", "delete successfully");
                map.put("trades", tradeRepository.searchTradeById(trade.getId()));

                System.out.println(trade.getId() + " delete succeeded");

                content = objectMapper.writeValueAsString(map);
                response.setContent(content);
            } else {
                System.out.println(trade.getId() + " delete failed, id not exist");
                response.setContent("id not exist");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setContent(e.getMessage());
        }
        return response;
    }


   private Response tryToTrade(Request request) {

       String card2Id = request.getContent().replace("\"", ""); // -d
       Trade trade;

       Response response = new Response();
       response.setStatusCode(StatusCode.OK);
       response.setContentType(ContentType.APPLICATION_JSON);

       try {
           trade = new Trade(); //??????????????????
           trade.setId(request.getPath().replace("/tradings/", "")); //Trade id from path
           String tradeId = trade.getId(); // trade id

           User user = MemorySession.get(request.getToken());
           //trade.setuId(user.getId());

           Card card2 = tradeRepository.findCardByCardId(card2Id); // card2 --> object
           Long card2Uid = card2.getuId();
           Card card1 = tradeRepository.findCardIdByTradeId(tradeId); // card1 --> object.  for to get id
           Long card1Uid = card1.getuId();

           //compare if uid is the same
           if (card2Uid.longValue() == card1Uid.longValue()) { //longValue --> L --> l
               //if same --> not allowed to trade with the same person
               response.setContent("Not allowed to trade with yourself!");
           } else { //else --not the same --> can trade
               //?????????????????????????????? -d card2,?????????????????? determine whether the card (-d card2) to be brought in for trading, if meet the requirements
               String type; //card2
               Trade trade1 = tradeRepository.searchTradeById(tradeId); // trade1 to get the card type
               if(card2.getName().toLowerCase().contains("spell")){
                   type = "spell";
               } else{
                   type = "monster";
               }

               if( type.equals(trade1.getType())  && card2.getDamage() >= trade1.getMinimumDamage()) {

                   tradeRepository.updateUserIdByCardId(card2Uid, card1.getId());
                   tradeRepository.updateUserIdByCardId(card1Uid, card2Id);

                   tradeRepository.updateTradeStatus(tradeId);

                   response.setContentType(ContentType.APPLICATION_JSON);
                   response.setContent("trade successfully!");
               } else {
                   response.setContentType(ContentType.APPLICATION_JSON);
                   response.setContent("condition not fulfilled!");
               }
           }
       } catch (Exception e) {
           e.printStackTrace();
           response.setContent(e.getMessage());
       }
       return response;
   }
       //?????????trade id --> ????????????
       //???????????????-d????????????  ???????????????id
       //get -d???uId  --??? SELECT * FROM CARD WHERE ID = ? ??????????????????--???+?????? findUserByCardId ????????????
       //compare uid if the same
       //if same --> not allowed to trade with urself
       //else --not the same --> ????????????
       //DB--> updateUserIdByCardId
       //1?????????????????????uid????????? ??????????????????uid
       //2.??????????????????uid????????? ???2????????????uid
       // trade successfully
       //??????????????????
       //update -- state = 1;

    private Response searchUserTrade(Request request) { //search for trade based on users
        ObjectMapper objectMapper = new ObjectMapper();
        List<Trade> tradeList;

        Response response = new Response();
        response.setStatusCode(StatusCode.OK);
        response.setContentType(ContentType.APPLICATION_JSON);
        try{
            User user = MemorySession.get(request.getToken());//user?????? ??????????????????
            tradeList = tradeRepository.findTradeByUserId(user.getId()); //??????????????????????????????????????? tradeList ???????????????
            String content = "";

            content = objectMapper.writeValueAsString(tradeList);
            response.setContent("Here is " + user.getUsername() + "'s trade(s): " + content);

        } catch (Exception e) {
            e.printStackTrace();
            response.setContent(e.getMessage());
        }
        return response;
    }

    private Response createTrade(Request request) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = request.getContent();
        Trade trade; //define a trade object

        Response response = new Response();
        response.setStatusCode(StatusCode.OK);
        response.setContentType(ContentType.APPLICATION_JSON);

        try {
            trade = objectMapper.readValue(json, Trade.class); //JSON parameters are passed to the trade object

            User user = MemorySession.get(request.getToken());
            trade.setuId(user.getId()); // get the user who build the trade from checkFile

            if (tradeRepository.save(trade)){ // save --> TradeDbRepository
                response.setStatusCode(StatusCode.CREATED); //1
                String content = "";
                Map map = new HashMap();
                map.put("msg","created successfully");
                map.put("trades", trade);
                content = objectMapper.writeValueAsString(map);
                response.setContent(content);
            }else{
                response.setContentType(ContentType.APPLICATION_JSON);
                response.setContent("trade already exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setContent(e.getMessage());
        }
        return response;
    }



}
