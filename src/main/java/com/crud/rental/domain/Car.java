package com.crud.rental.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "CARS")
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CAR_ID", unique = true)
    private Long id;

    @Column(name = "COLOUR", nullable = false)
    private String colour;

    @Column(name = "CAR_BRAND", nullable = false)
    private String carBrand;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

    @Column(name = "KILOMETERS", nullable = false)
    private int kilometers;

    @Column(name = "AVAILABILITY", nullable = false)
    @ColumnDefault("true")
    private boolean availability;

    @Column(name = "FUEL", nullable = false)
    private String fuel; // rodzaj paliwa

    @Column(name = "FUEL_CAPACITY", nullable = false)
    private double fuelCapacity;
}
