package org.example.application.packages.repository;

import org.example.application.packages.model.Packages;
import org.example.application.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class PackageDbRepository implements PackageRepository {

    @Override
    public boolean save(List<Packages> packagesList) throws Exception {
        Connection connection = DatabaseUtil.getConnection();

        String insertUserSql = "INSERT INTO PACKAGES(ID, NAME, DAMAGE) VALUES(?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertUserSql)) {
            for (Packages packages:packagesList){
                ps.setString(1, packages.getId());
                ps.setString(2, packages.getName());
                ps.setFloat(3, packages.getDamage());
                ps.execute();
            }
        } catch (Exception e) {
            throw e;
        }

        return true;
    }
}