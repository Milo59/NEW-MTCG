package org.example.application.sessions.repository;

import org.example.application.user.model.User;
import org.example.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SessionMemoryRepository implements SessionRepository{

    @Override
    public boolean login(User user)throws Exception {
        Connection connection = DatabaseUtil.getConnection();

        //verify whether the user already exists
        String userFindByUsernameSql = "SELECT COUNT(USERNAME) NUMBER FROM USERS WHERE USERNAME = ? AND PASSWORD = ?";// SELECT COUNT 统计
        try (PreparedStatement ps = connection.prepareStatement(userFindByUsernameSql)) {
            ps.setString(1, user.getUsername());// getUsername import from User class
            ps.setString(2, user.getHashPassword());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                if (rs.getInt("number") >= 1) { //already exist--can login  getInt（）方法：检索当前行中指定列的值；若该列的int值为null，则返回0；
                    return true;
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return false;
    }
}
