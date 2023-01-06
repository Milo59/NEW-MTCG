package org.example.application.trade.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.application.card.model.Card;
import org.example.application.sessions.model.MemorySession;
import org.example.application.trade.model.Trade;
import org.example.application.trade.repository.TradeDbRepository;
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

        if (method.equals(Method.POST.method ) && path.equals("/tradings/6cd85277-4590-49d4-b0cf-ba0a921faad0")){
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
        Trade trade;//defi null

        Response response = new Response();
        response.setStatusCode(StatusCode.OK);
        response.setContentType(ContentType.APPLICATION_JSON);

        try {
            trade = new Trade(); //内存分配地址
            trade.setId(request.getPath().replace("/tradings/", "")); //从路径截取id
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
           trade = new Trade(); //内存分配地址
           trade.setId(request.getPath().replace("/tradings/", "")); //从路径截取Trade id
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
               tradeRepository.updateUserIdByCardId(card2Uid, card1.getId());
               tradeRepository.updateUserIdByCardId(card1Uid, card2Id);

               tradeRepository.updateTradeStatus(tradeId);

               response.setContentType(ContentType.APPLICATION_JSON);
               response.setContent("trade successfully!");

           }
       } catch (Exception e) {
           e.printStackTrace();
           response.setContent(e.getMessage());
       }


       return response;
   }
       //路径取trade id --> 交易记录
       //检测脚本取-d传的参数  要交易的卡id
       //get -d的uId  --》 SELECT * FROM CARD WHERE ID = ? 得到卡的记录--》+新增 findUserByCardId 方法实现
       //compare uid if the same
       //if same --> not allowed to trade with urself

       //else --not the same --> 实现交易

       //DB--> updateUserIdByCardId
       //1。第二部的卡的uid更新为 第三部的卡的uid
       //2.第三步的卡的uid更新为 第2部的卡的uid
       // trade successfully
       //
       //更新交易状态
       //update -- state = 1;

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
