package org.example.application.game.repository;

import org.example.application.game.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserMemoryRepository implements UserRepository {
    // House --> User 以下为示例代码
    private final List<User> users;

    public UserMemoryRepository() {
        this.users = new ArrayList<>();
        this.users.add(new User());
        this.users.add(new User());
        this.users.add(new User());
    }


    @Override
    public User save(User user) {
        this.users.add(user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return this.users;
    }

    @Override
    public User delete(User user) {
        this.users.remove(user);
        return user;
    }
}
