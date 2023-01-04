package org.example.application.packages.repository;

import org.example.application.card.model.Card;
import org.example.application.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PackageDbRepository implements PackageRepository {

    //private int i = 1;

    //create new packages
    @Override
    public boolean save(List<Card> packagesList) throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DatabaseUtil.getConnection();
            connection.setAutoCommit(false);
            //String packageId = (i++) + "";
            String packageId = UUID.randomUUID().toString(); // UUID --> unique id
            String insertPackageSql = "INSERT INTO PACKAGES(ID,NAME) VALUES(?,?)";
            ps = connection.prepareStatement(insertPackageSql);
            ps.setString(1, packageId);
            ps.setString(2, packageId);
            ps.execute();

            String insertCardSql = "INSERT INTO CARD(ID, P_ID, NAME, DAMAGE) VALUES(?, ?, ?, ?)";
            ps = connection.prepareStatement(insertCardSql);
            for (Card card : packagesList) {
                ps.setString(1, card.getId());
                ps.setString(2, packageId);
                ps.setString(3, card.getName());
                ps.setFloat(4, card.getDamage());
                ps.execute();
            }
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
            if (null != ps) {
                ps.close();
            }
        }


        return true;
    }

    @Override
    public List<Card> acquirePackages(Long userId) throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        List<Card> cards = new ArrayList<>();
        try {
            connection = DatabaseUtil.getConnection();
            connection.setAutoCommit(false);

            String pid = "";
            String sql = "select * from packages where state = 1 limit 1";
            ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pid = rs.getString("id");
            }

            sql = "update packages set state = 0 where id = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1,pid);
            ps.execute();

            sql = "update card set u_id = ? where p_id = ?";
            ps = connection.prepareStatement(sql);
            ps.setLong(1,userId);
            ps.setString(2,pid);
            ps.execute();

            sql = "update users set money=money-5 where id = ?";
            ps = connection.prepareStatement(sql);
            ps.setLong(1,userId);
            ps.execute();

            // findAll example
            sql = "SELECT * FROM card where p_id = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1,pid);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Card card = new Card();
                card.setId(resultSet.getString("id"));
                card.setDamage(resultSet.getFloat("damage"));
                card.setName(resultSet.getString("name"));
                cards.add(card);
            }
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
            if (null != ps) {
                ps.close();
            }
        }
        return cards;
    }

    @Override
    public boolean checkPackage() throws Exception {  //检查包剩下数量是否大于1 能否被买
        Connection conn = DatabaseUtil.getConnection();
        String sql = "SELECT COUNT(1) AS NUM FROM PACKAGES WHERE STATE=1";
        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("NUM")>=1;
                }
            }
        }
        return false;
    }

}