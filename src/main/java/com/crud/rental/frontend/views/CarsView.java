package com.crud.rental.frontend.views;

import com.crud.rental.domain.Car;
import com.crud.rental.domain.CarDto;
import com.crud.rental.mapper.CarMapper;
import com.crud.rental.service.CarService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
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

        add(new Button("Add Car", e -> openAddCarDialog()));

        configureGrid();
        add(grid);

        refreshGrid();
    }

    private void configureGrid() {
        grid.setColumns("id", "colour", "carBrand", "price", "kilometers", "availability", "fuel", "fuelCapacity");
        grid.addComponentColumn(car -> createEditButton(car));
        grid.addComponentColumn(car -> createDeleteButton(car));
    }

    private Button createEditButton(CarDto car) {
        return new Button("Edit", e -> openEditCarDialog(car));
    }

    private Button createDeleteButton(CarDto car) {
        return new Button("Delete", e -> {
            carService.deleteCarById(car.getId());
            refreshGrid();
        });
    }

    private void openAddCarDialog() {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField colour = new TextField("Colour");
        TextField carBrand = new TextField("Brand");
        NumberField price = new NumberField("Price");
        NumberField kilometers = new NumberField("Kilometers");
        TextField fuel = new TextField("Fuel");
        NumberField fuelCapacity = new NumberField("Fuel Capacity");

        Button saveButton = new Button("Save", e -> {
            CarDto carDto = new CarDto();
            carDto.setColour(colour.getValue());
            carDto.setCarBrand(carBrand.getValue());
            carDto.setPrice(price.getValue() != null ? BigDecimal.valueOf(price.getValue()) : BigDecimal.ZERO);
            carDto.setKilometers(kilometers.getValue() != null ? kilometers.getValue().intValue() : 0);
            carDto.setAvailability(true);
            carDto.setFuel(fuel.getValue());
            carDto.setFuelCapacity(fuelCapacity.getValue() != null ? fuelCapacity.getValue() : 0.0);

            Car car = carMapper.mapToCar(carDto);
            carService.saveCar(car);

            refreshGrid();
            dialog.close();
        });

        formLayout.add(colour, carBrand, price, kilometers, fuel, fuelCapacity, saveButton);
        dialog.add(formLayout);
        dialog.open();
    }

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

        Button saveButton = new Button("Save", e -> {
            carDto.setColour(colour.getValue());
            carDto.setCarBrand(carBrand.getValue());
            carDto.setPrice(BigDecimal.valueOf(price.getValue()));
            carDto.setKilometers(kilometers.getValue().intValue());
            carDto.setFuel(fuel.getValue());
            carDto.setFuelCapacity(fuelCapacity.getValue());

            Car car = carMapper.mapToCar(carDto);
            carService.saveCar(car);

            refreshGrid();
            dialog.close();
        });

        formLayout.add(colour, carBrand, price, kilometers, fuel, fuelCapacity, saveButton);
        dialog.add(formLayout);
        dialog.open();
    }

    private void refreshGrid() {
        List<CarDto> carDtos = carService.getAllCars().stream()
                .map(carMapper::mapToCarDto)
                .collect(Collectors.toList());
        grid.setItems(carDtos);
    }
}
