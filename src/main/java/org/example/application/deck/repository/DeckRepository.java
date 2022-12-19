package org.example.application.deck.repository;

import org.example.application.user.model.User;

import java.util.List;

public interface DeckRepository {

    List<String> findDeckCard(User user) throws Exception;

    void configureDeck(User user,List<String> cardList) throws Exception;

}
