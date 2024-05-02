package com.crud.rental.repository;

import com.crud.rental.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {
Optional<Car>findById(Long carId);
}
