package org.example.application.battles.repository;

import org.example.application.battles.model.Log;
import org.example.application.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class BattlesDbRepository implements BattlesRepository{
    @Override
    public void insert(Log log) throws Exception {
        Connection conn = DatabaseUtil.getConnection();
        String sql = "INSERT INTO battle_log(user1,user2,winner,user1_card_name,user1_card_damage,user2_card_name,user2_card_damage) VALUES(?,?,?,?,?,?,?)";

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, log.getUser1());
            ps.setString(2, log.getUser2());
            ps.setString(3, log.getWinner());
            ps.setString(4, log.getUser1CardName());
            ps.setFloat(5, log.getUser1CardDamage());
            ps.setString(6, log.getUser2CardName());
            ps.setFloat(7, log.getUser2CardDamage());
            ps.execute();
        }
    }
}
