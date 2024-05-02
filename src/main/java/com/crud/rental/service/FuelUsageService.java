package com.crud.rental.service;

import com.crud.rental.domain.FuelUsage;
import com.crud.rental.domain.Reservation;
import com.crud.rental.repository.FuelUsageRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service

@RequiredArgsConstructor
public class FuelUsageService {
    private final FuelUsageRepository fuelUsageRepository;
   public FuelUsage saveFuelUsage(final FuelUsage fuelUsage){
       return fuelUsageRepository.save(fuelUsage);
   }

    public BigDecimal calculateTotalCost(int fuelConsumption, BigDecimal fuelPrice) {
        return fuelPrice.multiply(BigDecimal.valueOf(fuelConsumption));
    }

    private BigDecimal calculateFuelConsumption(int startKm, int endKm, int fuelUsed) {
        int distanceTraveled = endKm - startKm;
        return BigDecimal.valueOf((double) fuelUsed / distanceTraveled * 100);
    }



}
