package com.crud.rental.mapper;

import com.crud.rental.domain.Car;
import com.crud.rental.domain.CarDto;
import com.crud.rental.exception.CarNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class CarMapper {
    public Car mapToCar(final CarDto carDto) throws CarNotFoundException{
        Car car = new Car();
        car.setId(carDto.getId());
        car.setColour(carDto.getColour());
        car.setCarBrand(carDto.getCarBrand());
        car.setPrice(carDto.getPrice());
        car.setAvailability(carDto.isAvailability());
        car.setFuel(carDto.getFuel());
        car.setFuelCapacity((carDto.getFuelCapacity()));
        return car;
    }
    public  CarDto mapToCarDto(Car car) {
        CarDto carDto = new CarDto();
        carDto.setId(car.getId());
        carDto.setColour(car.getColour());
        carDto.setCarBrand(car.getCarBrand());
        carDto.setPrice(car.getPrice());
        carDto.setAvailability(car.isAvailability());
        carDto.setFuel(car.getFuel());
        carDto.setFuelCapacity((car.getFuelCapacity())); // Konwertujemy fuelCapacity na String
        return carDto;
    }
    public List<CarDto> mapToCarDtoList(List<Car> carList) {
        CarMapper carMapper = new CarMapper();
        return carList.stream()
                .map(carMapper::mapToCarDto) // Wywołanie mapToCarDto dla każdego obiektu Car w liście
                .collect(Collectors.toList());

    }}
