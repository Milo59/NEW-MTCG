package org.example.application.battles.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.application.battles.model.Log;
import org.example.application.battles.repository.BattlesRepository;
import org.example.application.card.model.Card;
import org.example.application.deck.repository.DeckRepository;
import org.example.application.sessions.model.MemorySession;
import org.example.application.user.model.User;
import org.example.application.user.repository.UserRepository;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.http.ContentType;
import org.example.server.http.Method;
import org.example.server.http.StatusCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattlesController {
    private static List<User> deckUser = new ArrayList<>();

    private final BattlesRepository battlesRepository;
    private final DeckRepository deckRepository;
    private final UserRepository userRepository;

    public BattlesController(BattlesRepository battlesRepository, DeckRepository deckRepository,UserRepository userRepository) {
        this.battlesRepository = battlesRepository;
        this.deckRepository = deckRepository;
        this.userRepository = userRepository;
    }

    public Response handle(Request request) {
        String method = request.getMethod();
        String path = request.getPath();
        if (method.equals(Method.POST.method)) {
            return battle(request);
        }
        Response response = new Response();
        response.setStatusCode(StatusCode.METHODE_NOT_ALLOWED);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setContent(request.getMethod() + ": Not allowed for " + request.getPath());

        return response;
    }

    private Response battle(Request request) {
        ObjectMapper objectMapper = new ObjectMapper();

        Response response = new Response();
        response.setStatusCode(StatusCode.OK);
        response.setContentType(ContentType.APPLICATION_JSON);

        try {
            User user = MemorySession.get(request.getToken());
            deckUser.add(user);
            if (deckUser.size()>=2){
                //start battle

                User user1 = deckUser.get(0);
                User user2 = deckUser.get(1);

                List<Card> deckCard1 = deckRepository.findDeckCard(user1);
                List<Card> deckCard2 = deckRepository.findDeckCard(user2);
                if (deckCard1.size()!=4 || deckCard2.size()!=4){
                    response.setContent("Not in conformity with battle");
                }

                //Round start
                int round=1;
                Random rand = new Random();
                while (round<=100 && deckCard1.size()>0 && deckCard2.size()>0){
                    Card card1 = deckCard1.get(rand.nextInt(deckCard1.size()));
                    Card card2 = deckCard2.get(rand.nextInt(deckCard2.size()));
                    Card card = round(card1, card2);
                    round++;
                    if (card==null){
                        //回合平局
                        continue;
                    }else{
                        //记录回合日志TODO
                        Log log = new Log();
                        log.setUser1(user1.getUsername());
                        log.setUser2(user2.getUsername());
                        log.setUser1CardName(card1.getName());
                        log.setUser1CardDamage(card1.getDamage());
                        log.setUser2CardName(card2.getName());
                        log.setUser2CardDamage(card2.getDamage());
                        //修改用户分数
                        if(deckCard1.contains(card)){
                            //user1本回合获胜
                            log.setWinner(user1.getUsername());
                            userRepository.addScore(user1.getId());
                            userRepository.reduceScore(user2.getId());
                            deckCard2.remove(card2);
                        }else{
                            //user2本回合获胜
                            log.setWinner(user2.getUsername());
                            userRepository.addScore(user2.getId());
                            userRepository.reduceScore(user1.getId());
                            deckCard1.remove(card1);
                        }
                        battlesRepository.insert(log);
                    }
                }
                response.setContent("battle end");
                deckUser = new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setContent(e.getMessage());
        }
        return response;
    }

    //round
    private Card round(Card card1,Card card2){
        if (card1.getName().contains("Spell") && card2.getName().contains("Spell")){
            //法术战斗
            if (card1.getName().contains("Fire")){
                if (card2.getName().contains("Fire")){

                }else if (card2.getName().contains("Water")){
                    card1.setDamage(card1.getDamage()/2);
                    card2.setDamage(card2.getDamage()*2);
                }else{
                    //正常
                    card1.setDamage(card1.getDamage()*2);
                    card2.setDamage(card2.getDamage()/2);
                }
            }else if(card1.getName().contains("Water")){
                if (card2.getName().contains("Fire")){
                    card1.setDamage(card1.getDamage()*2);
                    card2.setDamage(card2.getDamage()/2);
                }else if (card2.getName().contains("Water")){

                }else{
                    card1.setDamage(card1.getDamage()/2);
                    card2.setDamage(card2.getDamage()*2);
                }
            }else if(card1.getName().contains("Regular")){
                if (card2.getName().contains("Fire")){
                    card1.setDamage(card1.getDamage()/2);
                    card2.setDamage(card2.getDamage()*2);
                }else if (card2.getName().contains("Water")){
                    card1.setDamage(card1.getDamage()*2);
                    card2.setDamage(card2.getDamage()/2);
                }else{

                }
            }
        }else if(card1.getName().contains("Spell") || card2.getName().contains("Spell")){
            //混合战斗
            if (card1.getName().equals("Knight") && card2.getName().equals("WaterSpell")){
                return card2;
            }
            if (card2.getName().equals("Knight") && card1.getName().equals("WaterSpell")){
                return card1;
            }

            if (card1.getName().equals("Kraken")){
                return card1;
            }

            if (card2.getName().equals("Kraken")){
                return card2;
            }

            if (card1.getName().contains("Spell")){
                //card1 is Spell
                if (card1.getName().contains("Fire")){
                    if (card2.getName().contains("Fire")){

                    }else if (card2.getName().contains("Water")){
                        card1.setDamage(card1.getDamage()/2);
                        card2.setDamage(card2.getDamage()*2);
                    }else{
                        //正常
                        card1.setDamage(card1.getDamage()*2);
                        card2.setDamage(card2.getDamage()/2);
                    }
                }else if(card1.getName().contains("Water")){
                    if (card2.getName().contains("Fire")){
                        card1.setDamage(card1.getDamage()*2);
                        card2.setDamage(card2.getDamage()/2);
                    }else if (card2.getName().contains("Water")){

                    }else{
                        card1.setDamage(card1.getDamage()/2);
                        card2.setDamage(card2.getDamage()*2);
                    }
                }else if(card1.getName().contains("Regular")){
                    if (card2.getName().contains("Fire")){
                        card1.setDamage(card1.getDamage()/2);
                        card2.setDamage(card2.getDamage()*2);
                    }else if (card2.getName().contains("Water")){
                        card1.setDamage(card1.getDamage()*2);
                        card2.setDamage(card2.getDamage()/2);
                    }else{

                    }
                }
            }else {
                //card2 is Spell
                if (card2.getName().contains("Fire")){
                    if (card1.getName().contains("Fire")){

                    }else if (card1.getName().contains("Water")){
                        card1.setDamage(card1.getDamage()*2);
                        card2.setDamage(card2.getDamage()/2);
                    }else{
                        card1.setDamage(card1.getDamage()/2);
                        card2.setDamage(card2.getDamage()*2);
                    }
                }else if(card2.getName().contains("Water")){
                    if (card1.getName().contains("Fire")){
                        card1.setDamage(card1.getDamage()/2);
                        card2.setDamage(card2.getDamage()*2);
                    }else if (card1.getName().contains("Water")){

                    }else{
                        card1.setDamage(card1.getDamage()*2);
                        card2.setDamage(card2.getDamage()/2);
                    }
                }else if(card2.getName().contains("Regular")){
                    if (card1.getName().contains("Fire")){
                        card1.setDamage(card1.getDamage()*2);
                        card2.setDamage(card2.getDamage()/2);
                    }else if (card1.getName().contains("Water")){
                        card1.setDamage(card1.getDamage()/2);
                        card2.setDamage(card2.getDamage()*2);
                    }else{

                    }
                }
            }
        }else{
            //Monster Fights
            if (card1.getName().equals("Goblins") && card2.getName().equals("Gragons")){
                return card2;
            }

            if (card2.getName().equals("Goblins") && card1.getName().equals("Gragons")){
                return card1;
            }

            if (card1.getName().equals("Orks") && card2.getName().equals("Wizzard")){
                return card2;
            }

            if (card2.getName().equals("Orks") && card1.getName().equals("Wizzard")){
                return card1;
            }

            if (card1.getName().equals("Dragens") && card2.getName().equals("FireElves")){
                return card2;
            }

            if (card2.getName().equals("Dragens") && card1.getName().equals("FireElves")){
                return card1;
            }
        }

        if (card1.getDamage()>card2.getDamage()){
            return card1;
        }else if(card1.getDamage()==card2.getDamage()){
            return null;
        }else {
            return card2;
        }
    }
}
