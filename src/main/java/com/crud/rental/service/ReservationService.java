package com.crud.rental.service;

import com.crud.rental.domain.Car;
import com.crud.rental.domain.Option;
import com.crud.rental.domain.Reservation;
import com.crud.rental.domain.User;
import com.crud.rental.repository.CarRepository;
import com.crud.rental.repository.OptionRepository;
import com.crud.rental.repository.ReservationRepository;
import com.crud.rental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Transactional
    public void addReservation(Reservation reservation) {
        // Pobierz użytkownika i samochód z bazy danych
        User user = userRepository.findById(reservation.getUser().getId()).orElseThrow(() -> new RuntimeException("User not found"));
        Car car = carRepository.findById(reservation.getCar().getId()).orElseThrow(() -> new RuntimeException("Car not found"));

        List<Option> managedOptions = reservation.getOptions().stream()
                .map(option -> optionRepository.findById(option.getId()).orElseThrow())
                .collect(Collectors.toList());
        reservation.setOptions(managedOptions);

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

    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }


    public void endReservation(Long reservationId, int mileage, String region) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new IllegalArgumentException("Invalid reservation Id:" + reservationId));
        reservation.setMileage(mileage);
        reservation.setEnded(true); // Assuming you have a field to mark reservation as ended
        reservationRepository.save(reservation);
    }
}
