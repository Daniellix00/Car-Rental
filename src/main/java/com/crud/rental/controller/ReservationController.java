package com.crud.rental.controller;

import com.crud.rental.domain.Car;
import com.crud.rental.domain.Option;
import com.crud.rental.domain.Reservation;
import com.crud.rental.domain.ReservationDto;
import com.crud.rental.exception.CarNotFoundException;
import com.crud.rental.exception.OptionNotFoundException;
import com.crud.rental.exception.ReservationNotFoundException;
import com.crud.rental.mapper.ReservationMapper;
import com.crud.rental.repository.ReservationRepository;
import com.crud.rental.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationMapper reservationMapper;

    @PostMapping
    public ResponseEntity<Void> addReservation(@RequestBody ReservationDto reservationDto) {
        Reservation reservation = reservationMapper.mapToReservation(reservationDto);
        reservationService.addReservation(reservation);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "{reservationId}")
    public ResponseEntity<ReservationDto> getReservation(@PathVariable Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = reservationService.getReservation(reservationId);
        ReservationDto reservationDto = reservationMapper.mapToReservationDto(reservation);
        return ResponseEntity.ok(reservationDto);
    }

    @PutMapping("/{reservationId}")
    public ResponseEntity<Void> updateReservation(@PathVariable Long reservationId, @RequestBody ReservationDto reservationDto) throws ReservationNotFoundException {
        Reservation reservation = reservationMapper.mapToReservation(reservationDto);
        reservation.setId(reservationId);
        reservationService.updateReservation(reservation);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) throws ReservationNotFoundException {
        reservationService.deleteReservation(reservationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add/{reservationId}/{carId}")
    public ResponseEntity<ReservationDto> addCarToReservation(@PathVariable Long reservationId, @PathVariable Long carId) throws ReservationNotFoundException, CarNotFoundException {
        Reservation reservation = reservationService.addCarToReservation(carId, reservationId);
        return ResponseEntity.ok(reservationMapper.mapToReservationDto(reservation));
    }

    @PostMapping("/add/Option/{reservationId}/{optionId}")
    public ResponseEntity<ReservationDto> addOptionToReservation(@PathVariable Long reservationId, @PathVariable Long optionId) throws ReservationNotFoundException, OptionNotFoundException {
        Reservation reservation = reservationService.addOptionToReservation(reservationId, optionId);
        return ResponseEntity.ok(reservationMapper.mapToReservationDto(reservation));
    }

    @DeleteMapping("/delete/{reservationId}/{carId}")
    public ResponseEntity<ReservationDto> removeCarFromReservation(@PathVariable Long reservationId, @PathVariable Long carId)
            throws ReservationNotFoundException, CarNotFoundException {
        Car car = reservationService.getCarFromReservation(reservationId, carId);
        Reservation reservation = reservationService.deleteCarFromReservation(reservationId, carId);
        return ResponseEntity.ok(reservationMapper.mapToReservationDto(reservation));
    }
}
