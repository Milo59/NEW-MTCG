package org.example.application.user.repository;


import org.example.application.stats.model.Stats;
import org.example.application.user.model.User;

import java.util.List;

public interface UserRepository {

    User findByUsername(String username) throws Exception;

    boolean save(User user) throws Exception;

    List<User> findAll() throws Exception;

    void delete(User user) throws Exception;

    void update(User user) throws Exception;

    Stats rank(User user) throws Exception;

    List<Stats> rank() throws Exception;

}

