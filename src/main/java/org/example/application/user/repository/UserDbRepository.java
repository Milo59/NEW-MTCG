package org.example.application.user.repository;

import org.example.application.user.model.User;
import org.example.application.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDbRepository class
 *
 * @author Xinlan Zhang
 * @date 2022/12/13
 */
public class UserDbRepository implements UserRepository {

    @Override
    public List<User> findAll() throws Exception {
        Connection connection = DatabaseUtil.getConnection();
        String userFindAllSql = "SELECT * FROM USERS";
        List<User> users = new ArrayList<>();
        try (
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(userFindAllSql)
        ) {
            while (rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setMoney(rs.getInt("money"));
                users.add(user);
            }
        } catch (Exception e) {
            throw e;
        }
        return users;
    }

    @Override
    public User findByUsername(String username) throws Exception {
        // findByUsername example
        Connection connection = DatabaseUtil.getConnection();
        String userFindByUsernameSql = "SELECT * FROM USERS WHERE USERNAME = ?";
        User user = null;
        try (PreparedStatement ps = connection.prepareStatement(userFindByUsernameSql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getLong("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setMoney(rs.getInt("money"));
                    user.setName(rs.getString("name"));
                    user.setBio(rs.getString("bio"));
                    user.setImage(rs.getString("image"));
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return user;
    }

    @Override
    public boolean save(User user) throws Exception {
        Connection connection = DatabaseUtil.getConnection(); //get db conn

        //verify whether the user already exists
        String userFindByUsernameSql = "SELECT COUNT(USERNAME) NUMBER FROM USERS WHERE USERNAME = ?";//SELECT COUNT 统计条数（行）
        try (PreparedStatement ps = connection.prepareStatement(userFindByUsernameSql)) {
            ps.setString(1, user.getUsername()); //根据传入的数据 设置参数 传到 ？（70）
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                if (rs.getInt("number") >= 1) { //already exist--cannot sign up again
                    return false;
                }
            }
        } catch (Exception e) {
            throw e;
        }
        String insertUserSql = "INSERT INTO USERS(USERNAME, PASSWORD,MONEY) VALUES(?, ?, ?)"; //
        try (PreparedStatement ps = connection.prepareStatement(insertUserSql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setInt(3, user.getMoney());
            ps.execute();
        } catch (Exception e) {
            throw e;
        }

        return true; //创建成功
    }

    @Override
    public void delete(User user) throws Exception { //additional feature
        Connection connection = DatabaseUtil.getConnection();
        String userDeleteByUsernameSql = "DELETE FROM USERS WHERE USERNAME = ?";
        try (PreparedStatement ps = connection.prepareStatement(userDeleteByUsernameSql)) {
            ps.setString(1, user.getUsername());
            ps.execute();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void update(User user) throws Exception { //edit user profile
        Connection connection = DatabaseUtil.getConnection();
        String userDeleteByUsernameSql = "UPDATE USERS SET NAME = ? , BIO = ?, IMAGE = ? WHERE USERNAME = ?";
        try (PreparedStatement ps = connection.prepareStatement(userDeleteByUsernameSql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getBio());
            ps.setString(3, user.getImage());
            ps.setString(4, user.getUsername());
            ps.execute();
        } catch (Exception e) {
            throw e;
        }
    }
}
