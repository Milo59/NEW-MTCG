package org.example.application.housing.controller;

import org.example.application.housing.Repository.HouseRepository;
import org.example.application.housing.model.House;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.http.StatusCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HouseControllerTest {

    HouseController houseController;

    @Mock
    HouseRepository houseRepository;

    @Test
    void testGetHouses() {
        // Arrange
        List<House> houses = new ArrayList<>();
        houses.add(new House(1, 20, "Austria"));
        houses.add(new House(2, 50, "Austria"));
        houses.add(new House(4, 100, "Belgium"));

        when(houseRepository.findAll()).thenReturn(houses);

        houseController = new HouseController(houseRepository);

        Request request = new Request();
        request.setMethod("GET");
        request.setPath("/houses");

        // Act
        Response response = houseController.handle(request);

        // Assert
        assertEquals(StatusCode.OK.code, response.getStatus());
        assertEquals(houses.toString(), response.getContent());
        verify(houseRepository.findAll(), times(1));
    }
}