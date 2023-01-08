package org.example.application.card.repository;

import org.example.application.card.model.Card;
import org.example.application.user.model.User;

import java.util.List;

public interface CardRepository {

    List<Card> findCardByUserId(Long userid) throws Exception;// Returns the list card collection

}
