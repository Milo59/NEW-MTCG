package org.example.application.trade.repository;

import org.example.application.card.model.Card;
import org.example.application.trade.model.Trade;

import java.util.List;

public interface TradeRepository {

    List<Trade> findTradeByUserId (Long userid) throws Exception;

    Trade searchTradeById(String id) throws Exception; //used in delete

    Card findCardByCardId(String id) throws Exception; // get uid from card id

    Card findCardByUserId (Long userid) throws Exception;

    Card findCardIdByTradeId (String id) throws Exception;

    boolean updateUserIdByCardId(Long card, String id) throws Exception;


    boolean save(Trade trade) throws Exception ;

    boolean delete(Trade trade) throws Exception;
}
