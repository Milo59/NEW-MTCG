package org.example.application.trade.controller;

import com.fasterxml.jackson.core.type.TypeReference;
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
/*
        if (method.equals(Method.DELETE.method ) && path.equals("/tradings/6cd85277-4590-49d4-b0cf-ba0a921faad0")){
            return deleteTrade(request);
        }

        if (method.equals(Method.POST.method ) && path.equals("/tradings/6cd85277-4590-49d4-b0cf-ba0a921faad0")){
            return tryToTrade(request);
        }
*/
        Response response = new Response();
        response.setStatusCode(StatusCode.METHODE_NOT_ALLOWED);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setContent(request.getMethod() + ": Not allowed for " + request.getPath());

        return response;
    }
/*
    private Response tryToTrade(Request request) {

    }

    private Response deleteTrade(Request request) {
    }*/

    private Response searchUserTrade(Request request) { //根据用户搜索交易
        ObjectMapper objectMapper = new ObjectMapper();
        List<Trade> tradeList;

        Response response = new Response();
        response.setStatusCode(StatusCode.OK);
        response.setContentType(ContentType.APPLICATION_JSON);
        try{
            User user = MemorySession.get(request.getToken());//user变量 获取用户信息
            tradeList = tradeRepository.findTradeByUserId(user.getId()); //把该用户扥所有交易放入名为 tradeList 的交易集合？
            String content = "";

            content = objectMapper.writeValueAsString(tradeList);
            response.setContent("Here is " + user.getUsername() + "'s trade(s): " + content);

        } catch (Exception e) {
            e.printStackTrace();
            response.setContent(e.getMessage());
        }
        return response;
    }

    private Response createTrade(Request request) { //创建交易
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
