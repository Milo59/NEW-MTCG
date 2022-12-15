package org.example.application.sessions.model;

import org.example.application.user.model.User;

import java.util.HashMap;
import java.util.Map;

public class MemorySession {
    private static final Map<String, User> map = new HashMap<>();

    public static User put(String authorization,User user){
        return map.put(authorization,user);
    }

    public static User remove(String authorization){
        return map.remove(authorization);
    }

    public static User get(String authorization){
        return map.get(authorization);
    }

    /**
     * number of people online
     * @return
     */
    public static int online(){
        return map.size();
    }
}
