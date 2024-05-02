package com.crud.rental.service;

import com.crud.rental.domain.Car;
import com.crud.rental.exception.CarNotFoundException;
import com.crud.rental.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;

    public Car saveCar(final Car car) {
      return carRepository.save(car);
    }
    public Car getCarById(final  Long carId) throws CarNotFoundException{
        return carRepository.findById(carId).orElseThrow(CarNotFoundException::new);
    }
    public void deleteCarById(final Long carId) throws CarNotFoundException {
        Car car = carRepository.findById(carId).orElseThrow(CarNotFoundException::new);
        carRepository.delete(car);
    }
    public List<Car> getAllCars(){
        return carRepository.findAll();
    }
    public Car updateCar(Car car) throws CarNotFoundException {
        Car updateCar = carRepository.findById(car.getId()).orElseThrow(CarNotFoundException::new);
        // Aktualizacja danych samochodu
        updateCar.setColour(car.getColour());
        updateCar.setCarBrand(car.getCarBrand());
        updateCar.setPrice(car.getPrice());
        updateCar.setKilometers(car.getKilometers());
        updateCar.setAvailability(car.isAvailability());
        updateCar.setFuel(car.getFuel());
        updateCar.setFuelCapacity(car.getFuelCapacity());
        // Zapisanie zaktualizowanego samochodu
        return carRepository.save(updateCar);
    }
    public List<Car> getAvailableCars(){
        return  carRepository.findAll().stream()
                .filter(Car::isAvailability)
                .collect(Collectors.toList());
    }
}
