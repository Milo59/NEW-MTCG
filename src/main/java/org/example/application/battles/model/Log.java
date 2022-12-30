package org.example.application.battles.model;

public class Log {
    private Long id;
    private String user1;
    private String user2;
    private String winner;
    private String user1CardName;
    private float user1CardDamage;
    private String user2CardName;
    private float user2CardDamage;

    public Log() {
    }

    public Log(Long id, String user1, String user2, String winner, String user1CardName, float user1CardDamage, String user2CardName, float user2CardDamage) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
        this.winner = winner;
        this.user1CardName = user1CardName;
        this.user1CardDamage = user1CardDamage;
        this.user2CardName = user2CardName;
        this.user2CardDamage = user2CardDamage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getUser1CardName() {
        return user1CardName;
    }

    public void setUser1CardName(String user1CardName) {
        this.user1CardName = user1CardName;
    }

    public float getUser1CardDamage() {
        return user1CardDamage;
    }

    public void setUser1CardDamage(float user1CardDamage) {
        this.user1CardDamage = user1CardDamage;
    }

    public String getUser2CardName() {
        return user2CardName;
    }

    public void setUser2CardName(String user2CardName) {
        this.user2CardName = user2CardName;
    }

    public float getUser2CardDamage() {
        return user2CardDamage;
    }

    public void setUser2CardDamage(float user2CardDamage) {
        this.user2CardDamage = user2CardDamage;
    }
}
