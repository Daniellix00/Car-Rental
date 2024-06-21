package com.crud.rental.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuelPriceDto {
    private String region;
    private String fuelType;
    private BigDecimal price;
}
