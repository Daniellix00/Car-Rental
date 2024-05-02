package com.crud.rental.service;
import com.crud.rental.domain.Car;
import com.crud.rental.domain.Option;
import com.crud.rental.domain.Damage;
import com.crud.rental.domain.Reservation;
import com.crud.rental.exception.CarNotFoundException;
import com.crud.rental.exception.OptionNotFoundException;
import com.crud.rental.exception.ReservationNotFoundException;
import com.crud.rental.repository.CarRepository;
import com.crud.rental.repository.DamageRespository;
import com.crud.rental.repository.ReservationRepository;
import com.helger.commons.annotation.VisibleForTesting;
import lombok.RequiredArgsConstructor;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final CarService carService;
    private final OptionService optionService;
    private final CarRepository carRepository;
    private final DamageRespository damageRespository;

    public void addReservation(final Reservation reservation) {
        LocalDate startDate = reservation.getStartDate();
        LocalDate endDate = reservation.getEndDate();
        reservationRepository.save(reservation);
        // Sprawdzenie, czy data endDate jest późniejsza niż data startDate
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Data końcowa musi być późniejsza niż data początkowa.");
        }
        // Dodanie rezerwacji
        reservationRepository.save(reservation);
    }

    public Reservation getReservation(final Long reservationId) throws ReservationNotFoundException {
        return reservationRepository.findById(reservationId).orElseThrow(ReservationNotFoundException::new);
    }

    public Reservation getReservationById(final Long reservationId) throws ReservationNotFoundException {
        return reservationRepository.findById(reservationId).orElseThrow(ReservationNotFoundException::new);
    }

    public Reservation updateReservation(Reservation reservation) throws ReservationNotFoundException {
        Reservation updateReservation = this.getReservationById(reservation.getId());
        LocalDate startDate = reservation.getStartDate();
        LocalDate endDate = reservation.getEndDate();
        // Dodatkowa walidacja dat - sprawdzenie, czy data końcowa nie jest wcześniejsza niż data początkowa
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Data końcowa nie może być wcześniejsza niż data początkowa.");
        }
        updateReservation.setStartDate(reservation.getStartDate());
        updateReservation.setEndDate(reservation.getEndDate());
        updateReservation.setTotalPrice(reservation.getTotalPrice());
        updateReservation.setStatus(reservation.isStatus());
        updateReservation.setCar(reservation.getCar());
        updateReservation.setDamages(reservation.getDamages());
        updateReservation.setUser(reservation.getUser());
        return reservationRepository.save(updateReservation);
    }

    public Reservation addCarToReservation(final Long carId, final Long reservationId) throws ReservationNotFoundException, CarNotFoundException {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFoundException::new);
        Car car = carRepository.findById(carId).orElseThrow(ReservationNotFoundException::new);
        if (!car.isAvailability()) {
            throw new IllegalStateException("Samochód nie jest dostępny.");
        }
        reservation.setCar(car);

        // Zaktualizuj status samochodu na niedostępny
        car.setReservation(reservation);
        car.setAvailability(false);
        carService.saveCar(car);
        reservation.setCar(car);
        return reservationRepository.save(reservation);
//Logika dodawania auta do rezerwacji polega na tym ze do jednej rezerwacji moze byc przypisane tylko jedno auto
//Jesli uzytkownik chce wypozyczyc dwa auta musi otworzyc nowa rezerwacje na drugie auto
//Zapewnia to prostszą kontrolę i obliczanie cen rezerwacji
    }

    public Reservation addOptionToReservation(Long reservationId, Long optionId) throws ReservationNotFoundException, OptionNotFoundException {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFoundException::new);
        Option option = optionService.getOptionById(optionId);
        List<Option> options = reservation.getOptions();
        if (options == null) {
            options = new ArrayList<>();
        }
        options.add(option);
      reservation.setOptions(options);
        return reservationRepository.save(reservation);
    }

    public BigDecimal calculateTotalPrice(Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFoundException::new);
        BigDecimal basePrice = calculateBasePrice(reservation);
        BigDecimal optionCosts = calculateOptionCosts(reservation);
        BigDecimal damageCosts = calculateDamageCosts(reservation);
        BigDecimal totalPrice = basePrice.add(optionCosts).add(damageCosts);
        reservation.setTotalPrice(totalPrice);
        reservationRepository.save(reservation);

        return totalPrice;
    }

    public BigDecimal calculateDamageCosts(Reservation reservation) {
        List<Damage> damages = reservation.getDamages();
        BigDecimal totalDamageCosts = BigDecimal.ZERO;
        for (Damage damage : damages) {
            BigDecimal repairCost = damage.getRepairCost();
            totalDamageCosts = totalDamageCosts.add(repairCost);
        }
        return totalDamageCosts;
    }

    public BigDecimal calculateOptionCosts(Reservation reservation) {
        List<Option> options = reservation.getOptions();
         BigDecimal totalOptionCosts = BigDecimal.ZERO;
        if (options != null) {
            for (Option option : options) {
                BigDecimal optionPrice = option.getPrice();
                totalOptionCosts = totalOptionCosts.add(optionPrice);
            }
        }
       return totalOptionCosts;
    }

    public BigDecimal calculateBasePrice(Reservation reservation) {
        Car car = reservation.getCar();
        LocalDate startDate = reservation.getStartDate();
        LocalDate endDate = reservation.getEndDate();

        long numberOfDays = ChronoUnit.DAYS.between(startDate, endDate);
        BigDecimal pricePerDay = car.getPrice();
        BigDecimal totalPrice = pricePerDay.multiply(BigDecimal.valueOf(numberOfDays));
        return totalPrice;
    }

    public void deleteReservation(final Long reservationId) throws ReservationNotFoundException {
        reservationRepository.delete(reservationRepository.findById(reservationId).orElseThrow(ReservationNotFoundException::new));
    }

    public Car getCarFromReservation(Long reservationId, Long carId) throws ReservationNotFoundException, CarNotFoundException {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFoundException::new);
        Car car = reservation.getCar();
        return car;
    }

    public Reservation deleteCarFromReservation(final Long reservationId, final Long carId) throws ReservationNotFoundException, CarNotFoundException {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFoundException::new);


        Car car = reservation.getCar();
        if (car != null && car.getId().equals(carId)) {
            reservation.setCar(null);
            car.setReservation(null);
            car.setAvailability(true);

            // Dodaj logi
            System.out.println("Car before save: " + car);
            car = carRepository.save(car);
            System.out.println("Car after save: " + car);
        } else {
            throw new CarNotFoundException();
        }

        return reservationRepository.save(reservation);
    }

    public Option getOptionFromReservation(Long reservationId, Long optionId) throws ReservationNotFoundException, OptionNotFoundException {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFoundException::new);
        List<Option> options = reservation.getOptions();
        for (Option option : options) {
            if (option.getId() == optionId) {
                return option;
            }
        }
        throw new OptionNotFoundException();
    }
    public Reservation deleteOptionFromReservation(final  long reservationId, final Long optionId) throws  ReservationNotFoundException, OptionNotFoundException {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFoundException::new);
        List<Option> options = reservation.getOptions();
        for (Option option : options) {
            if (option.getId() == optionId) {
                options.remove(option);
                reservation.setOptions(options);
               return reservationRepository.save(reservation);

            }
        }
        throw new OptionNotFoundException();
    }
    public void addDamageToDatabase(Damage damage) {
        damageRespository.save(damage);
    }
    public void addDamageToReservation(Long reservationId, Long damageId) throws ReservationNotFoundException{
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFoundException::new);
        Damage damage  = damageRespository.findById(damageId).orElseThrow(() -> new RuntimeException("No fault with the given ID was found"));
       List<Damage> damages = reservation.getDamages();
        if (damages == null) {
            damages = new ArrayList<>();
        }
        damages.add(damage);
        reservation.setDamages(damages);
        reservationRepository.save(reservation);
    }

}
