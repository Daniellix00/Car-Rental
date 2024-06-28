package com.crud.rental.frontend.views;

import com.crud.rental.domain.Car;
import com.crud.rental.domain.CarDto;
import com.crud.rental.mapper.CarMapper;
import com.crud.rental.service.CarService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
        grid.addComponentColumn(car -> createEditButton(car)).setHeader("Edit");
        grid.addComponentColumn(car -> createDeleteButton(car)).setHeader("Delete");
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
        TextField price = new TextField("Price");
        TextField kilometers = new TextField("Kilometers");
        ComboBox<String> fuel = new ComboBox<>("Fuel");

        fuel.setItems(fetchFuelTypes());
        TextField fuelCapacity = new TextField("Fuel Capacity");

        Button saveButton = new Button("Save", e -> {
            try {
                CarDto carDto = new CarDto();
                carDto.setColour(colour.getValue());
                carDto.setCarBrand(carBrand.getValue());
                carDto.setPrice(new BigDecimal(price.getValue()));
                carDto.setKilometers(Integer.parseInt(kilometers.getValue()));
                carDto.setAvailability(true);
                carDto.setFuel(fuel.getValue());
                carDto.setFuelCapacity(Double.parseDouble(fuelCapacity.getValue()));

                Car car = carMapper.mapToCar(carDto);
                carService.saveCar(car);

                Notification.show("Car added: " + carDto.getCarBrand() + ", " + carDto.getPrice() + ", " + carDto.getKilometers() + ", " + carDto.getFuelCapacity());
                refreshGrid();
                dialog.close();
            } catch (NumberFormatException ex) {
                Notification.show("Please enter valid numbers for price, kilometers, and fuel capacity.");
            } catch (Exception ex) {
                Notification.show("Error: " + ex.getMessage());
            }
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
        TextField price = new TextField("Price", carDto.getPrice().toString());
        TextField kilometers = new TextField("Kilometers", String.valueOf(carDto.getKilometers()));
        ComboBox<String> fuel = new ComboBox<>("Fuel");
        fuel.setItems(fetchFuelTypes());
        fuel.setValue(carDto.getFuel());
        TextField fuelCapacity = new TextField("Fuel Capacity", String.valueOf(carDto.getFuelCapacity()));

        Button saveButton = new Button("Save", e -> {
            try {
                carDto.setColour(colour.getValue());
                carDto.setCarBrand(carBrand.getValue());
                carDto.setPrice(new BigDecimal(price.getValue()));
                carDto.setKilometers(Integer.parseInt(kilometers.getValue()));
                carDto.setFuel(fuel.getValue());
                carDto.setFuelCapacity(Double.parseDouble(fuelCapacity.getValue()));

                Car car = carMapper.mapToCar(carDto);
                carService.saveCar(car);

                Notification.show("Car updated: " + carDto.getCarBrand() + ", " + carDto.getPrice() + ", " + carDto.getKilometers() + ", " + carDto.getFuelCapacity());
                refreshGrid();
                dialog.close();
            } catch (NumberFormatException ex) {
                Notification.show("Please enter valid numbers for price, kilometers, and fuel capacity.");
            } catch (Exception ex) {
                Notification.show("Error: " + ex.getMessage());
            }
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

    private List<String> fetchFuelTypes() {
        return List.of("95", "98", "ON", "ON+", "LPG");
    }
}
