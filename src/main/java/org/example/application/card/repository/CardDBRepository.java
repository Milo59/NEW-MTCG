package org.example.application.card.repository;


import org.example.DatabaseInit;
import org.example.application.card.model.Card;
import org.example.application.sessions.model.MemorySession;
import org.example.application.user.model.User;
import org.example.application.utils.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CardDBRepository implements CardRepository{


    @Override
    public List<Card> findCardByUserId(Long userid) throws Exception {
        // find
        Connection conn = DatabaseUtil.getConnection();
        String findCardByUserIdSql = "SELECT * FROM CARD WHERE U_ID = ?";

        List<Card> cards= new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(findCardByUserIdSql)) {
            ps.setLong(1, userid);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Card card = new Card();
                    card.setId(rs.getString("id"));
                    card.setName(rs.getString("name"));
                    card.setDamage(rs.getFloat("damage"));

                    cards.add(card);
                }
            }

        }
        return cards;
    }
}
