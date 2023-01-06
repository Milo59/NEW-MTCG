package org.example.application.sessions.repository;

import org.example.application.user.model.User;

public interface SessionRepository {

    boolean login(User user)throws Exception;
}
