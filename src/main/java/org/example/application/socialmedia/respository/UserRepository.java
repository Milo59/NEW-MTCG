package org.example.application.socialmedia.respository;

import org.example.application.socialmedia.model.User;

import java.util.List;

public interface UserRepository {

    List<User> findAll();

    User findByUsername(String username);

    User save(User user);

    User delete(User user);
}
