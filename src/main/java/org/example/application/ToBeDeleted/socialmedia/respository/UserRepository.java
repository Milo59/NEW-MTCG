package org.example.application.ToBeDeleted.socialmedia.respository;

import org.example.application.ToBeDeleted.socialmedia.model.User;

import java.util.List;

public interface UserRepository {

    List<User> findAll();

    User findByUsername(String username);

    User save(User user);

    User delete(User user);
}
