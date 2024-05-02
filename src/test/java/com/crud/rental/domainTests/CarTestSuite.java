package com.crud.rental.domainTests;

import com.crud.rental.domain.Car;
import com.crud.rental.exception.CarNotFoundException;
import com.crud.rental.repository.CarRepository;
import com.crud.rental.service.CarService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CarTestSuite {
    @Autowired
    CarRepository carRepository;
    @Autowired
    CarService carService;
    @AfterEach
    public void cleanUp() {
        carRepository.deleteAll();
        assertTrue(carRepository.findAll().isEmpty());
    }
    @BeforeEach
    public void setup(){
        carRepository.deleteAll();
    }
    Car car = new Car(1L, "Red", "Toyota", BigDecimal.valueOf(100), 50000, true, "Gasoline", 50.0);

    @Test
    public void testSaveCar() {
        // Given & When
        Car savedCar = carService.saveCar(car);

        // Then
        assertTrue(savedCar.isAvailability());
        assertNotNull(savedCar.getId(), "Car ID should not be null"); // Sprawdza czy identyfikator samochodu nie jest null
        assertEquals("Red", savedCar.getColour());
        assertEquals("Toyota", savedCar.getCarBrand());
        assertEquals(BigDecimal.valueOf(100), savedCar.getPrice());
        assertEquals(50000, savedCar.getKilometers());
        assertTrue(savedCar.isAvailability());
        assertEquals("Gasoline", savedCar.getFuel());
        assertEquals(50.0, savedCar.getFuelCapacity());
    }
    @Test
    public void testGetCarById() throws CarNotFoundException {
        // Given & When
        Car savedCar = carService.saveCar(car);
        Long carId = savedCar.getId();
        Car retrievedCar = carService.getCarById(carId);

        // Then
        assertNotNull(retrievedCar);
        assertEquals(savedCar.getId(), retrievedCar.getId());
        assertEquals(savedCar.getColour(), retrievedCar.getColour());
        assertEquals(savedCar.getCarBrand(), retrievedCar.getCarBrand());

    }
    @Test
    public void testDeleteCarById() throws CarNotFoundException{
        //Given & When
        Car savedCar = carService.saveCar(car);
        Long carId = savedCar.getId();
        carService.deleteCarById(carId);
        //Then
        assertThrows(CarNotFoundException.class, () -> carService.getCarById(carId));
    }
    @Test
    public void testUpdateCar() throws CarNotFoundException {
        // Given
        Car existingCar = new Car();
        existingCar.setColour("Red");
        existingCar.setCarBrand("Toyota");
        existingCar.setPrice(BigDecimal.valueOf(100.00));
        existingCar.setKilometers(50000);
        existingCar.setAvailability(true);
        existingCar.setFuel("Gasoline");
        existingCar.setFuelCapacity(50.0);

         carRepository.save(existingCar);

         //When
        existingCar.setPrice(BigDecimal.valueOf(200.00));
        existingCar.setKilometers(60000);
        carRepository.save(existingCar);
        Optional<Car> retrievedCar = carRepository.findById(existingCar.getId());

        // Then
        assertTrue(retrievedCar.isPresent());
        assertEquals(existingCar.getId(), retrievedCar.get().getId());
        assertEquals(existingCar.getKilometers(), retrievedCar.get().getKilometers());
        assertEquals(0, existingCar.getPrice().compareTo(retrievedCar.get().getPrice()));
    }

}

