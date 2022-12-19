package org.example.application.deck.repository;

import org.apache.commons.lang3.StringUtils;
import org.example.application.user.model.User;
import org.example.application.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DeckDbRepository implements DeckRepository {
    @Override
    public List<String> findDeckCard(User user) throws Exception {
        Connection connection = DatabaseUtil.getConnection();
        String findUserDeckCardSql = "SELECT ID FROM CARD where U_ID = ? AND DECK = 1";
        List<String> cards = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(findUserDeckCardSql)) {
            ps.setLong(1, user.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    cards.add(rs.getString("ID"));
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
