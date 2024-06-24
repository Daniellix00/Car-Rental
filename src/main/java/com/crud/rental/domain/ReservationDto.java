package com.crud.rental.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalPrice;
    private boolean status;

}
