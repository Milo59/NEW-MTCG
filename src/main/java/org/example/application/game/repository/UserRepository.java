package org.example.application.game.repository;


import java.util.List;

public interface UserRepository {

    User save(User user);

    List<User> findAll();

    User delete(User user);

}

