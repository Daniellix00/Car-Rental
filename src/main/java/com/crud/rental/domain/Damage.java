package com.crud.rental.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "DAMAGE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Damage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;
    @Column(name = "PRICE", nullable = false)
    private BigDecimal repairCost;
    @ManyToOne
    @JoinColumn(name = "RESERVATION_ID")
    private Reservation reservation;
    public Damage(BigDecimal repairCost) {
        this.repairCost = repairCost;
    }
}
