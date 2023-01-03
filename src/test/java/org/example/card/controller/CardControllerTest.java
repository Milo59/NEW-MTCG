package org.example.card.controller;

import org.example.application.card.controller.CardController;
import org.example.application.card.model.Card;
import org.example.application.card.repository.CardDBRepository;
import org.example.application.card.repository.CardRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CardControllerTest {
    CardController cardController;

    CardRepository cardRepository = new CardDBRepository();

    @Test
    void testGetUserCard() throws Exception {
        List<Card> cards = cardRepository.findCardByUserId(44l);
        System.out.println(cards);
    }
}
