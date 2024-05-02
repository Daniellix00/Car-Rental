package com.crud.rental.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "RESERVATIONS")

@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "START_DATE", nullable = false)
    private LocalDate startDate;
    @Column(name = "END_DATE", nullable = false)
    private LocalDate endDate;
    @Column(name = "TOTAL_PRICE")
    private BigDecimal totalPrice;
    @Column(name = "STATUS", nullable = false)
    private boolean status;
    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    @JoinColumn(name = "CAR_ID")
    private Car car;
    @OneToMany(mappedBy = "reservation")
    private List<Damage> damages;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    @JoinTable(
            name = "RESERVATION_OPTION",
            joinColumns = @JoinColumn(name = "RESERVATION_ID"),
            inverseJoinColumns = @JoinColumn(name = "OPTION_ID")
    )
    private List<Option> options;

    public Reservation(Long id, LocalDate startDate, LocalDate endDate, Boolean status, List<Option> options) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.options = options;
    }



    public Reservation() {
        this.options = new ArrayList<>();
    }
}
