package org.example.application.housing.model;

public class House {

    private int rooms;

    private int squareMeter;

    private String country;

    public House(int rooms, int squareMeter, String country) {
        this.rooms = rooms;
        this.squareMeter = squareMeter;
        this.country = country;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public int getSquareMeter() {
        return squareMeter;
    }

    public void setSquareMeter(int squareMeter) {
        this.squareMeter = squareMeter;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "House{" +
                "rooms=" + rooms +
                ", squareMeter=" + squareMeter +
                ", country='" + country + '\'' +
                '}';
    }
}
