package org.example.application.trade.repository;

import org.example.application.card.model.Card;
import org.example.application.trade.model.Trade;

import java.util.List;

public interface TradeRepository {

    List<Trade> findTradeByUserId (Long userid) throws Exception;

    boolean save(List<Trade> tradeList) throws Exception ;
}
