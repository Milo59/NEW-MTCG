package org.example.application.game.model;
import com.fasterxml.jackson.annotation.JsonFormat;
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class User {

    private String username;

    private String password;

    private int coins = 20;

    public User() { //Constructor1
    }

    public User(String username, String password) { //Constructor2
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
