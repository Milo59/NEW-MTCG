package org.example.application.user.model;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.nio.charset.StandardCharsets;
import com.google.common.hash.Hashing;

@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class User {
    private Long id;

    private String username;
    private String password;

    private String passwordHash;
    private String name;
    private String bio;
    private String image;
    private int money = 20;

    public User() {
    }

    public User(Long id, String username, String password, String name, String bio, String image, int money) {
        this.id = id;
        this.username = username;
        this.password = password;
        //this.passwordHash = passwordHash;
        this.name = name;
        this.bio = bio;
        this.image = image;
        this.money = money;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHashPassword(){ // Password encryption
        return Hashing.sha256() // Hashing dependency added
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
        /*String passwordHash = Hashing.sha256() // Hashing dependency added
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
        return passwordHash;*/
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public String getImage() {
        return image;
    }

    public int getMoney() {
        return money;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
