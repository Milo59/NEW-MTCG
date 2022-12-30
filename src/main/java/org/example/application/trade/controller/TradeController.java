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
        if (method.equals(Method.PUT.method ) && path.equals("/tradings/6cd85277-4590-49d4-b0cf-ba0a921faad0")){
            return createTrade(request);
        }

        Response response = new Response();
        response.setStatusCode(StatusCode.METHODE_NOT_ALLOWED);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setContent(request.getMethod() + ": Not allowed for " + request.getPath());

        return response;
    }

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
            //maybe加一个 if else setContent No Trade ？

        } catch (Exception e) {
            e.printStackTrace();
            response.setContent(e.getMessage());
        }
        return response;
    }

    private Response createTrade(Request request) { //创建交易
        ObjectMapper objectMapper = new ObjectMapper();
        String json = request.getContent();
        List<Trade> tradeList;

        Response response = new Response();
        response.setStatusCode(StatusCode.OK);
        response.setContentType(ContentType.APPLICATION_JSON);
        try {
            tradeList = objectMapper.readValue(json, new TypeReference<List<Trade>>() {});

            MemorySession.get(request.getToken());

            if (tradeRepository.save(tradeList)){ // save --> create
                response.setStatusCode(StatusCode.CREATED); //1
                String content = "";
                Map map = new HashMap();
                map.put("msg","created successfully");
                map.put("trades",tradeList);
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
