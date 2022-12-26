package org.example.application.stats.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Stats {

    private int rank;

    private String username;

    private String name;
    private String bio;
    private String image;
    private int money;
    private int scored;

    public Stats() {
    }

    public Stats(int rank, String username, String name, String bio, String image, int money, int scored) {
        this.rank = rank;
        this.username = username;
        this.name = name;
        this.bio = bio;
        this.image = image;
        this.money = money;
        this.scored = scored;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getScored() {
        return scored;
    }

    public void setScored(int scored) {
        this.scored = scored;
    }
}
