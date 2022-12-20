package org.example.application.deck.model;

public class Deck {

    private Long id;
    private Long uId;
    private Long cId;
    private String username;
    private String cardName;

    public Deck() {
    }

    public Deck(Long id, Long uId, Long cId, String username, String cardName) {
        this.id = id;
        this.uId = uId;
        this.cId = cId;
        this.username = username;
        this.cardName = cardName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getuId() {
        return uId;
    }

    public void setuId(Long uId) {
        this.uId = uId;
    }

    public Long getcId() {
        return cId;
    }

    public void setcId(Long cId) {
        this.cId = cId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
