package com.crud.rental.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@Data
@Entity
@Table(name = "FUEL_USAGE")
@NoArgsConstructor
@AllArgsConstructor
public class FuelUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "START_KM", nullable = true)
    private int startKm;
    @Column(name = "END_KM", nullable = true)
    private int endKm;
    @Column(name = "FUEL_PRICE_PER_LITER", nullable = false)
    private BigDecimal fuelPrice;
   @Column(name = "TOTAL_COST")
    private BigDecimal totalCost;
    @ManyToOne
    @JoinColumn(name = "RESERVATION_ID")
    private Reservation reservation;
    @Column(name = "FUEL_CONSUMPTION")
    private BigDecimal fuelConsumption;
    @Column(name = "FUEL_TYPE")
    private String fuelType;
    public FuelUsage(int startKm, int endKm, BigDecimal fuelPrice, BigDecimal totalCost, BigDecimal fuelConsumption, String fuelType, Reservation reservation) {
        this.startKm = startKm;
        this.endKm = endKm;

        this.fuelPrice = fuelPrice;
        this.totalCost = totalCost;
        this.fuelConsumption = fuelConsumption;
        this.fuelType = fuelType;
        this.reservation = reservation;
    }

}
