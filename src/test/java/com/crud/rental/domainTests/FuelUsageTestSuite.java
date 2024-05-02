package com.crud.rental.domainTests;

import com.crud.rental.domain.FuelUsage;
import com.crud.rental.repository.FuelUsageRepository;
import com.crud.rental.service.FuelUsageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class FuelUsageTestSuite {
    @Autowired
    FuelUsageRepository fuelUsageRepository;
    @Autowired
    FuelUsageService fuelUsageService;
    @AfterEach
    public void cleanUp(){
      fuelUsageRepository.deleteAll();
      assertTrue(fuelUsageRepository.findAll().isEmpty());
    }
    @BeforeEach
    public void setUp(){
        fuelUsageRepository.deleteAll();
    }
    @Test
    public void testSaveFuelUsage() {
        // Given
        FuelUsage fuelUsage = new FuelUsage(1000, 1500, new BigDecimal("5.50"), null, new BigDecimal("50.0"), "Diesel", null);

        // When
        FuelUsage savedFuelUsage = fuelUsageRepository.save(fuelUsage);

        // Then
        FuelUsage found = fuelUsageRepository.findById(savedFuelUsage.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(1000, found.getStartKm());
        assertEquals(1500, found.getEndKm());
        assertEquals(0, new BigDecimal("5.50").compareTo(found.getFuelPrice()));
        assertEquals(0, new BigDecimal("50.0").compareTo(found.getFuelConsumption()));
    }
    @Test
    public void testCalculateTotalCost() {
        // Given
        FuelUsage fuelUsage = new FuelUsage();
        int fuelConsumption = 50; // Zużyte paliwo w litrach
        BigDecimal fuelPrice = new BigDecimal("5.50"); // Cena paliwa za litr

        // When

        BigDecimal totalCost = fuelUsageService.calculateTotalCost(fuelConsumption, fuelPrice);

        // Then
        assertEquals(new BigDecimal("275.00"), totalCost); // Oczekiwany koszt paliwa
    }

}
