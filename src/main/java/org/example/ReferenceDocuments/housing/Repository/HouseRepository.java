package org.example.ReferenceDocuments.housing.Repository;

import org.example.ReferenceDocuments.housing.model.House;

import java.util.List;

public interface HouseRepository {
    House save(House house);

    List<House> findAll();

    House delete(House house);
}
