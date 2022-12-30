package org.example.application.trade;

import org.example.application.sessions.model.MemorySession;
import org.example.application.trade.controller.TradeController;
import org.example.application.trade.repository.TradeDbRepository;
import org.example.application.trade.repository.TradeRepository;
import org.example.server.Application;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.http.ContentType;
import org.example.server.http.StatusCode;

public class TradeApp implements Application {

    private TradeController tradeController;

    public TradeApp(){
        TradeRepository tradeRepository = (TradeRepository) new TradeDbRepository();
        this.tradeController = new TradeController(tradeRepository);
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
            return this.tradeController.handle(request);  // go go go
        }
    }
}
