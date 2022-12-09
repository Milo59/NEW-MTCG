package org.example.application.game.repository;


import org.example.application.game.model.User;

import java.util.List;

public interface UserRepository {

    User save(User user);

    List<User> findAll();

    User delete(User user);

}

