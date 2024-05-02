package com.crud.rental.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OptionDto {
    private Long id;
    private String name;
    private BigDecimal price;

}
