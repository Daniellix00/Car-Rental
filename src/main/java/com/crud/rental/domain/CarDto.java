package com.crud.rental.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDto {
    private Long id;
    private String colour;
    private String carBrand;
    private int Kilometers;
    private BigDecimal price;
    private boolean availability;
    private String fuel;
    private double fuelCapacity;
}
