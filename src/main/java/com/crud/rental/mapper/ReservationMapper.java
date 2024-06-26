package com.crud.rental.mapper;

import com.crud.rental.domain.Car;
import com.crud.rental.domain.Reservation;
import com.crud.rental.domain.ReservationDto;
import com.crud.rental.domain.User;
import com.crud.rental.service.CarService;
import com.crud.rental.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationMapper {

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    public Reservation mapToReservation(ReservationDto reservationDto) {
        Reservation reservation = new Reservation();
        reservation.setId(reservationDto.getId());
        reservation.setStartDate(reservationDto.getStartDate());
        reservation.setEndDate(reservationDto.getEndDate());
        reservation.setTotalPrice(reservationDto.getTotalPrice());
        reservation.setStatus(reservationDto.isStatus());

        User user = userService.getUserById(reservationDto.getUserId());
        reservation.setUser(user);

        Car car = carService.getCarById(reservationDto.getCarId());
        reservation.setCar(car);

        return reservation;
    }

    public ReservationDto mapToReservationDto(Reservation reservation) {
        return new ReservationDto(
                reservation.getId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getTotalPrice(),
                reservation.isStatus(),
                reservation.getUser().getId(),
                reservation.getCar().getId()
        );
    }

    public List<ReservationDto> mapToReservationDtoList(List<Reservation> reservationList) {
        return reservationList.stream()
                .map(this::mapToReservationDto)
                .collect(Collectors.toList());
    }
}
