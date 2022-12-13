package org.example.application.game.repository;

import org.example.application.game.model.User;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserMemoryRepository implements UserRepository {

    private final List<User> users;

    public UserMemoryRepository() {
        this.users = new ArrayList<>();
    }

    @Override
    public List<User> findAll() {
        return this.users;
    }

    @Override
    public User findByUsername(String username) {
        for (User user: this.users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User save(User user) {
       /* try (Statement stmt2 = conn.createStatement()) {
            stmt2.execute(
                    """
                        INSERT INTO users(username, password)
                        VALUES('marvin', 'password');
                        """
            );
        }*/

        return user;
    }

    @Override
    public User delete(User user) {
        if (this.users.contains(user)) {
            this.users.remove(user);
        }

        return user;
    }
}
