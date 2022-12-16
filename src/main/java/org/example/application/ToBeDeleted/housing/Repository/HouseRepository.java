package org.example.application.ToBeDeleted.housing.Repository;

import org.example.application.ToBeDeleted.housing.model.House;

import java.util.List;

public interface HouseRepository {
    House save(House house);

    List<House> findAll();

    House delete(House house);
}
