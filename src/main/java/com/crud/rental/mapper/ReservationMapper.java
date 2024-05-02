package com.crud.rental.mapper;

import com.crud.rental.domain.Car;
import com.crud.rental.domain.Reservation;
import com.crud.rental.domain.ReservationDto;
import com.crud.rental.exception.CarNotFoundException;
import com.crud.rental.exception.ReservationNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationMapper {
    public Reservation mapToReservation(ReservationDto reservationDto) {
        Reservation reservation = new Reservation();
        reservation.setId(reservationDto.getId());
        reservation.setStartDate(reservationDto.getStartDate());
        reservation.setEndDate(reservationDto.getEndDate());
        reservation.setTotalPrice(reservationDto.getTotalPrice());
        reservation.setStatus(reservationDto.isStatus());
        return reservation;
    }

    public ReservationDto mapToReservationDto(Reservation reservation) {
       return new ReservationDto(
               reservation.getId(),
               reservation.getStartDate(),
               reservation.getEndDate(),
               reservation.getTotalPrice(),
               reservation.isStatus());
    }

    public List<ReservationDto> mapToReservationDtoList(List<Reservation> reservationList) {
        return reservationList.stream()
                .map(this::mapToReservationDto)
                .collect(Collectors.toList());
    }

    }

