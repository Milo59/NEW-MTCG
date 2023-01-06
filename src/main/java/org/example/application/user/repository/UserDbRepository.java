package org.example.application.user.repository;

import org.example.application.stats.model.Stats;
import org.example.application.user.model.User;
import org.example.DatabaseUtil;

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
        String insertUserSql = "INSERT INTO USERS(USERNAME, PASSWORD,MONEY) VALUES(?, ?, ?)"; //创建user
        try (PreparedStatement ps = connection.prepareStatement(insertUserSql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getHashPassword());
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

    @Override
    public Stats rank(User user) throws Exception {
        Connection connection = DatabaseUtil.getConnection();
        String userFindByUsernameSql = "SELECT DENSE_RANK() OVER (ORDER BY SCORED DESC) AS RANK, * FROM USERS";
        Stats stats = null;
        try (PreparedStatement ps = connection.prepareStatement(userFindByUsernameSql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    if (rs.getString("username").equals(user.getUsername())){
                        stats = new Stats();
                        stats.setUsername(rs.getString("username"));
                        stats.setName(rs.getString("name"));
                        stats.setBio(rs.getString("bio"));
                        stats.setImage(rs.getString("image"));
                        stats.setMoney(rs.getInt("money"));
                        stats.setRank(rs.getInt("rank"));
                        stats.setScored(rs.getInt("scored"));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return stats;
    }

    @Override
    public List<Stats> rank() throws Exception {
        Connection connection = DatabaseUtil.getConnection();
        String userFindByUsernameSql = "SELECT DENSE_RANK() OVER (ORDER BY SCORED DESC) AS RANK, * FROM USERS";
        List<Stats> statsList = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(userFindByUsernameSql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Stats stats = new Stats();
                    stats.setUsername(rs.getString("username"));
                    stats.setName(rs.getString("name"));
                    stats.setBio(rs.getString("bio"));
                    stats.setImage(rs.getString("image"));
                    stats.setMoney(rs.getInt("money"));
                    stats.setRank(rs.getInt("rank"));
                    stats.setScored(rs.getInt("scored"));
                    statsList.add(stats);
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return statsList;
    }

    @Override
    public void addScore(Long userid) throws Exception {
        Connection connection = DatabaseUtil.getConnection();
        String findUserDeckCardSql = "update users set scored = scored+3 where id = ?"; // +5 +3
        try (PreparedStatement ps = connection.prepareStatement(findUserDeckCardSql)) {
            ps.setLong(1, userid);
            ps.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void reduceScore(Long userid) throws Exception {
        Connection connection = DatabaseUtil.getConnection();
        String findUserDeckCardSql = "update users set scored = scored-5 where id = ?";
        try (PreparedStatement ps = connection.prepareStatement(findUserDeckCardSql)) {
            ps.setLong(1, userid);
            ps.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }
}
