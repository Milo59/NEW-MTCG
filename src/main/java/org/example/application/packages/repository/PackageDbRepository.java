package org.example.application.packages.repository;

import org.example.application.card.model.Card;
import org.example.application.packages.model.Packages;
import org.example.application.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.UUID;

public class PackageDbRepository implements PackageRepository {

    @Override
    public boolean save(List<Card> packagesList) throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DatabaseUtil.getConnection();
            connection.setAutoCommit(false);
            String packageId = UUID.randomUUID().toString();
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
}