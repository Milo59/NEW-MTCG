package org.example.server;

import org.example.application.GameApp;
import org.example.application.battles.BattlesApp;
import org.example.application.card.CardApp;
import org.example.application.deck.DeckApp;
import org.example.application.packages.PackageApp;
import org.example.application.scoreboard.ScoreBoardApp;
import org.example.application.sessions.SessionApp;
import org.example.application.stats.StatsApp;
import org.example.application.trade.TradeApp;
import org.example.application.transactions.TransactionsApp;
import org.example.application.user.UserApp;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.exception.UnsupportedProtocolException;
import org.example.server.util.RequestBuilder;
import org.example.server.util.RequestReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RequestHandler implements Runnable {

    private BufferedReader in;
    private PrintWriter out;

    private final Socket socket;
    private final Application application;

    public RequestHandler(Socket socket, Application application) {
        this.socket = socket;
        this.application = application;
    }

    @Override
    public void run() { //jump to here from server
        try {
            Request request = getRequest();
            Response response = null;
            String path = request.getPath();
            //Determine the path
            if(path.equals("/")){
                // default Handler
                response = new GameApp().indexHandle(request);
            }else if (path.startsWith("/users")) {
                //eg. Handler detects users path, call userApp.handle()
                UserApp userApp = new UserApp();
                response = userApp.handle(request);
            }else if(path.equals("/sessions")){
                response = new SessionApp().handle(request);
            }else if(path.equals("/packages")){
                response = new PackageApp().handle(request);
            }else if(path.equals("/transactions/packages")){
                response = new TransactionsApp().handle(request);
            }else if(path.startsWith("/deck")){
                response = new DeckApp().handle(request);
            } else if(path.equals("/cards")){
                    response = new CardApp().handle(request);
            }else if(path.equals("/stats")){
                response = new StatsApp().handle(request);
            }else if(path.equals("/score")){
                response = new ScoreBoardApp().handle(request);
            }else if(path.equals("/battles")){
                response = new BattlesApp().handle(request);
            }else if(path.startsWith("/tradings")){
                response = new TradeApp().handle(request);
            }else {
                // unrecognized request
                response = application.handle(request);
            }
            sendResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeRequest();
        }
    }

    private Request getRequest() throws IOException, UnsupportedProtocolException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String request = RequestReader.read(in);

        return RequestBuilder.build(request);
    }

    private void sendResponse(Response response) throws IOException {
        out = new PrintWriter(socket.getOutputStream(), true);

        out.write(response.toString());
    }

    private void closeRequest() {
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
