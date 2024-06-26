package com.crud.rental.service;

import com.crud.rental.domain.Car;
import com.crud.rental.domain.Reservation;
import com.crud.rental.domain.User;
import com.crud.rental.repository.CarRepository;
import com.crud.rental.repository.ReservationRepository;
import com.crud.rental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void addReservation(Reservation reservation) {
        // Pobierz użytkownika i samochód z bazy danych
        User user = userRepository.findById(reservation.getUser().getId()).orElseThrow(() -> new RuntimeException("User not found"));
        Car car = carRepository.findById(reservation.getCar().getId()).orElseThrow(() -> new RuntimeException("Car not found"));

        // Ustaw pobrane obiekty na rezerwację
        reservation.setUser(user);
        reservation.setCar(car);

        // Zapisz rezerwację
        reservationRepository.save(reservation);
    }

    public Reservation getReservation(Long id) {
        return reservationRepository.findById(id).orElseThrow(() -> new RuntimeException("Reservation not found"));
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public void updateReservation(Reservation reservation) {
        addReservation(reservation);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
