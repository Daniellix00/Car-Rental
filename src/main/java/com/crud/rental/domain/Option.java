package com.crud.rental.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "OPTIONS")
@NoArgsConstructor
@AllArgsConstructor
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;
    @ManyToMany(mappedBy = "options")
    private List<Reservation> reservations;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Option option)) return false;

        if (id != option.id) return false;
        if (!Objects.equals(name, option.name)) return false;
        if (!Objects.equals(price, option.price)) return false;
        return Objects.equals(reservations, option.reservations);
    }


}

