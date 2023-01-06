package org.example.ReferenceDocuments.housing.Repository;

import org.example.ReferenceDocuments.housing.model.House;

import java.util.ArrayList;
import java.util.List;

public class HouseMemoryRepository implements HouseRepository {

    private final List<House> houses;

    public HouseMemoryRepository() {
        this.houses = new ArrayList<>();
        this.houses.add(new House(1, 20, "Austria"));
        this.houses.add(new House(2, 50, "Austria"));
        this.houses.add(new House(4, 100, "Belgium"));
    }

    public House save(House house) {
        this.houses.add(house);

        return house;
    }

    public List<House> findAll() {
        return this.houses;
    }

    public House delete(House house) {
        this.houses.remove(house);

        return house;
    }
}
