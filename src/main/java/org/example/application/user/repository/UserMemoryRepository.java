package org.example.application.user.repository;

import org.example.application.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserMemoryRepository implements UserRepository{
    private final List<User> users;

    public UserMemoryRepository() {
        this.users = new ArrayList<>();
    }

    @Override
    public List<User> findAll()throws Exception {
        return this.users;
    }

    @Override
    public User findByUsername(String username) throws Exception {
       // this.users.size();//查看长度
        for (User user: this.users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public boolean save(User user) {
        if (!this.users.contains(user)) {
            return this.users.add(user);
        }
        return false;
    }

    @Override
    public void delete(User user)throws Exception {
        if (this.users.contains(user)) {
            this.users.remove(user);
        }
    }

    @Override
    public void update(User user) throws Exception {

    }
}
