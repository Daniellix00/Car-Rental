package com.crud.rental.frontend.views;

import com.crud.rental.domain.Car;
import com.crud.rental.domain.CarDto;
import com.crud.rental.mapper.CarMapper;
import com.crud.rental.service.CarService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Route("cars")
public class CarsView extends VerticalLayout {

    @Autowired
    private CarService carService;

    @Autowired
    private CarMapper carMapper;

    private Grid<CarDto> grid = new Grid<>(CarDto.class);

    public CarsView(CarService carService, CarMapper carMapper) {
        this.carService = carService;
        this.carMapper = carMapper;

        // Dodanie przycisku do dodawania nowego samochodu
        add(new Button("Add Car", e -> openAddCarDialog()));

        // Konfiguracja siatki
        configureGrid();
        add(grid);

        // Odświeżenie danych w siatce
        refreshGrid();
    }

    // Konfiguracja siatki
    private void configureGrid() {
        grid.setColumns("id", "colour", "carBrand", "price", "kilometers", "availability", "fuel", "fuelCapacity");
        grid.addComponentColumn(car -> createEditButton(car));
        grid.addComponentColumn(car -> createDeleteButton(car));
    }

    // Tworzenie przycisku edycji dla każdego wiersza
    private Button createEditButton(CarDto car) {
        return new Button("Edit", e -> openEditCarDialog(car));
    }

    // Tworzenie przycisku usuwania dla każdego wiersza
    private Button createDeleteButton(CarDto car) {
        return new Button("Delete", e -> {
            carService.deleteCarById(car.getId());
            refreshGrid();
        });
    }

    // Otwarcie dialogu do dodania nowego samochodu
    private void openAddCarDialog() {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField colour = new TextField("Colour");
        TextField carBrand = new TextField("Brand");
        NumberField price = new NumberField("Price");
        NumberField kilometers = new NumberField("Kilometers");
        TextField fuel = new TextField("Fuel");
        NumberField fuelCapacity = new NumberField("Fuel Capacity");

        // Przycisk do zapisywania nowego samochodu
        Button saveButton = new Button("Save", e -> {
            CarDto carDto = new CarDto();
            carDto.setColour(colour.getValue());
            carDto.setCarBrand(carBrand.getValue());
            carDto.setPrice(BigDecimal.valueOf(price.getValue()));
            carDto.setKilometers(kilometers.getValue().intValue());
            carDto.setAvailability(true);
            carDto.setFuel(fuel.getValue());
            carDto.setFuelCapacity(fuelCapacity.getValue());

            Car car = carMapper.mapToCar(carDto); // Mapowanie CarDto na Car
            carService.saveCar(car); // Zapisanie samochodu

            refreshGrid(); // Odświeżenie siatki
            dialog.close(); // Zamknięcie dialogu
        });

        formLayout.add(colour, carBrand, price, kilometers, fuel, fuelCapacity, saveButton);
        dialog.add(formLayout);
        dialog.open();
    }

    // Otwarcie dialogu do edycji istniejącego samochodu
    private void openEditCarDialog(CarDto carDto) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField colour = new TextField("Colour", carDto.getColour());
        TextField carBrand = new TextField("Brand", carDto.getCarBrand());
        NumberField price = new NumberField("Price");
        price.setValue(carDto.getPrice().doubleValue());
        NumberField kilometers = new NumberField("Kilometers");
        kilometers.setValue((double) carDto.getKilometers());
        TextField fuel = new TextField("Fuel", carDto.getFuel());
        NumberField fuelCapacity = new NumberField("Fuel Capacity");
        fuelCapacity.setValue(carDto.getFuelCapacity());

        // Przycisk do zapisywania edytowanego samochodu
        Button saveButton = new Button("Save", e -> {
            carDto.setColour(colour.getValue());
            carDto.setCarBrand(carBrand.getValue());
            carDto.setPrice(BigDecimal.valueOf(price.getValue()));
            carDto.setKilometers(kilometers.getValue().intValue());
            carDto.setFuel(fuel.getValue());
            carDto.setFuelCapacity(fuelCapacity.getValue());

            Car car = carMapper.mapToCar(carDto); // Mapowanie CarDto na Car
            carService.saveCar(car); // Zapisanie samochodu

            refreshGrid(); // Odświeżenie siatki
            dialog.close(); // Zamknięcie dialogu
        });

        formLayout.add(colour, carBrand, price, kilometers, fuel, fuelCapacity, saveButton);
        dialog.add(formLayout);
        dialog.open();
    }

    // Odświeżenie danych w siatce
    private void refreshGrid() {
        List<CarDto> carDtos = carService.getAllCars().stream()
                .map(carMapper::mapToCarDto)
                .collect(Collectors.toList());
        grid.setItems(carDtos);
    }
}
