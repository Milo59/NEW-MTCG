package org.example.application.deck.repository;

import org.example.application.card.model.Card;
import org.example.application.user.model.User;
import org.example.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DeckDbRepository implements DeckRepository {
    @Override
    public List<Card> findDeckCard(User user) throws Exception {
        Connection connection = DatabaseUtil.getConnection();
        String findUserDeckCardSql = "SELECT * FROM CARD where U_ID = ? AND DECK = 1";
        List<Card> cards = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(findUserDeckCardSql)) {
            ps.setLong(1, user.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Card card = new Card();
                    card.setId(rs.getString("id"));
                    card.setName(rs.getString("name"));
                    card.setDamage(rs.getFloat("damage"));
                    cards.add(card);
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return cards;
    }

    @Override
    public void configureDeck(User user, List<String> cardList) throws Exception {
        Connection connection = DatabaseUtil.getConnection();
        String findUserDeckCardSql = "UPDATE CARD SET DECK = 1 where U_ID = ? AND ID IN (?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(findUserDeckCardSql)) {
            ps.setLong(1, user.getId());
            ps.setString(2, cardList.get(0));
            ps.setString(3, cardList.get(1));
            ps.setString(4, cardList.get(2));
            ps.setString(5, cardList.get(3));
            ps.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }
}
