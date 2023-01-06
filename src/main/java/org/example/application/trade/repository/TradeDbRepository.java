package org.example.application.trade.repository;

import org.example.application.card.model.Card;
import org.example.application.trade.model.Trade;
import org.example.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TradeDbRepository implements TradeRepository{

    @Override
    public List<Trade> findTradeByUserId(Long userid) throws Exception {
        Connection conn = DatabaseUtil.getConnection();
        String findTradeByUserIdSql = "SELECT * FROM TRADES WHERE U_ID = ?";

        List<Trade> trades = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(findTradeByUserIdSql)) {
            ps.setLong(1, userid);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Trade trade = new Trade();
                    trade.setId(rs.getString("id"));
                    trade.setuId(rs.getLong("u_Id"));
                    trade.setCardToTrade(rs.getString("cardToTrade"));
                    trade.setType(rs.getString("type"));
                    trade.setMinimumDamage(rs.getInt("minimumdamage"));
                    trade.setStatus(rs.getInt("status"));

                    trades.add(trade);
                }
            }
        }
        return trades;
    }

    @Override
    public Trade searchTradeById(String id) throws Exception {
        Connection connection = DatabaseUtil.getConnection();

        String findByTradeIdSql = "SELECT * FROM trades WHERE id = ?";
        Trade trade = null;
        try(PreparedStatement ps = connection.prepareStatement(findByTradeIdSql)) {
            ps.setString(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    trade = new Trade();
                    trade.setId(rs.getString("id"));
                    trade.setuId(rs.getLong("u_id"));
                    trade.setCardToTrade(rs.getString("cardtotrade"));
                    trade.setType(rs.getString("type"));
                    trade.setMinimumDamage(rs.getInt("minimumdamage"));
                    trade.setStatus(rs.getInt("status"));

                }
            }
        }

        return trade;
    }

    @Override
    public Card findCardByCardId(String id) throws Exception {
        Connection connection = DatabaseUtil.getConnection();

        String findCardByCardIdSql = "SELECT * FROM CARD WHERE id = ?";
        Card card = null;
        try(PreparedStatement ps = connection.prepareStatement(findCardByCardIdSql)) {
            ps.setString(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    card = new Card();
                    card.setId(rs.getString("id"));
                    card.setuId(rs.getLong("u_id"));
                    card.setName(rs.getString("name"));
                    card.setDamage(rs.getFloat("damage"));
                  //  ps.execute();
                }
            }
        }
        return card;
    }

    @Override
    public Card findCardByUserId(Long userid) throws Exception {
        Connection connection = DatabaseUtil.getConnection();

        String findCardByUserIdSql = "SELECT * FROM CARD WHERE U_ID = ?";
        Card card = null;
        try(PreparedStatement ps = connection.prepareStatement(findCardByUserIdSql)) {
            ps.setLong(1, userid);
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    card = new Card();
                    card.setId(rs.getString("id"));
                    card.setuId(rs.getLong("u_id"));
                    card.setName(rs.getString("name"));
                    card.setDamage(rs.getFloat("damage"));
                    ps.execute();
                }
            }
        }
        return card;
    }

    @Override
    public Card findCardIdByTradeId(String id) throws Exception {
        Connection connection = DatabaseUtil.getConnection();

        String findCardIdByTradeIdSql = "SELECT * FROM Trades WHERE ID = ?";
        Card card = null;
        try(PreparedStatement ps = connection.prepareStatement(findCardIdByTradeIdSql)) {
            ps.setString(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    card = new Card();
                    card.setId(rs.getString("cardtotrade"));
                    card.setuId(rs.getLong("u_id"));
                    ps.execute();
                }
            }
        }
        return card;
    }

    @Override
    public void updateUserIdByCardId(Long card_userid, String id) throws Exception {
        Connection connection = DatabaseUtil.getConnection();
        String updateUserByCardIdSql = "update card set u_id = ? where id = ?";

        try (PreparedStatement ps = connection.prepareStatement(updateUserByCardIdSql)) {
            ps.setLong(1, card_userid);
            ps.setString(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void updateTradeStatus(String id) throws Exception { //--> searchTradeById
        Connection connection = DatabaseUtil.getConnection();
        String updateTradeStatusSql = "update trades set status = 1 where id = ?"; // already trade

        try(PreparedStatement ps = connection.prepareStatement(updateTradeStatusSql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }


    @Override
    public boolean save(Trade trade) throws Exception {  //保存交易信息  传trade参数
        Connection connection = DatabaseUtil.getConnection();
       // PreparedStatement ps = null;

        String insertTradeSql = "INSERT INTO TRADES(ID, CARDTOTRADE, TYPE, MINIMUMDAMAGE, U_ID, STATUS) VALUES(?, ?, ?, ?, ?, ?)";

        try(PreparedStatement ps = connection.prepareStatement(insertTradeSql)) {
            ps.setString(1, trade.getId());
            ps.setString(2, trade.getCardToTrade());
            ps.setString(3, trade.getType());
            ps.setInt(4, trade.getMinimumDamage());
            ps.setLong(5, trade.getuId());
            ps.setInt(6, trade.getStatus());
            ps.execute();
        }
        return true;
    }

    @Override
    public boolean delete(Trade trade) throws Exception {
        Connection conn = DatabaseUtil.getConnection();

        //verify whether the trade already exists
        String tradeFindByTradeIdSpl = "DELETE FROM TRADES WHERE ID = ?";
        try (PreparedStatement ps = conn.prepareStatement(tradeFindByTradeIdSpl)) {
            ps.setString(1, trade.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
        return true;
    }
}
