package org.example.application.card.repository;

import org.example.application.card.model.Card;
import org.example.application.user.model.User;

import java.util.List;

public interface CardRepository {

    List<Card> findCardByUserId(Long userid) throws Exception;//long类型from User Class  返回list 卡集合

}
