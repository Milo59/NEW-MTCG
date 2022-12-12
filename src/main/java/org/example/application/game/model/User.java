package org.example.application.game.model;
//import com.fasterxml.jackson.annotation.JsonFormat;
//@JsonFormat(with = JosnFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTISE)
public class User {

    private String username;

    private String password;

    public User() { //构造函数1
    }

    public User(String username, String password) { //构造函数2
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
