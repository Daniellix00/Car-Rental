package com.crud.rental.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FuelUsageDto {
    private long id;
    private int startKm;
    private int endKm;
    private double fuelPrice;
    private double totalCost;
    private List<Long> carId;
}
