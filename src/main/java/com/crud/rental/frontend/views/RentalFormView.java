package com.crud.rental.frontend.views;

import com.crud.rental.domain.Car;
import com.crud.rental.domain.Option;
import com.crud.rental.domain.Reservation;
import com.crud.rental.domain.ReservationDto;
import com.crud.rental.domain.User;
import com.crud.rental.mapper.ReservationMapper;
import com.crud.rental.service.CarService;
import com.crud.rental.service.OptionService;
import com.crud.rental.service.ReservationService;
import com.crud.rental.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Route("rental-form")
public class RentalFormView extends VerticalLayout {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private CarService carService;

    @Autowired
    private UserService userService;

    @Autowired
    private OptionService optionService;

    private Grid<ReservationDto> reservationGrid;

    public RentalFormView(ReservationService reservationService, ReservationMapper reservationMapper, CarService carService, UserService userService, OptionService optionService) {
        this.reservationService = reservationService;
        this.reservationMapper = reservationMapper;
        this.carService = carService;
        this.userService = userService;
        this.optionService = optionService;

        Button createReservationButton = new Button("Create Reservation", e -> openReservationDialog());
        add(createReservationButton);

        reservationGrid = new Grid<>(ReservationDto.class);
        reservationGrid.setColumns("id", "startDate", "endDate", "totalPrice", "status", "userId", "carId");
        add(reservationGrid);
    }

    private void openReservationDialog() {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        DatePicker startDate = new DatePicker("Start Date");
        DatePicker endDate = new DatePicker("End Date");
        ComboBox<Car> carComboBox = new ComboBox<>("Car");
        carComboBox.setItemLabelGenerator(Car::getCarBrand);
        carComboBox.setItems(carService.getAvailableCars());

        ComboBox<User> userComboBox = new ComboBox<>("User");
        userComboBox.setItemLabelGenerator(user -> user.getFirstName() + " " + user.getLastName());
        userComboBox.setItems(userService.getAllUsers());

        MultiSelectComboBox<Option> optionsComboBox = new MultiSelectComboBox<>("Options");
        optionsComboBox.setItemLabelGenerator(Option::getName);
        optionsComboBox.setItems(optionService.getAllOptions());

        Button saveButton = new Button("Save", e -> {
            Car selectedCar = carComboBox.getValue();
            User selectedUser = userComboBox.getValue();
            List<Option> selectedOptions = optionsComboBox.getSelectedItems().stream().toList();

            if (selectedCar == null) {
                Notification.show("Please select a car");
                return;
            }
            if (selectedUser == null) {
                Notification.show("Please select a user");
                return;
            }

            ReservationDto reservationDto = new ReservationDto();
            reservationDto.setStartDate(startDate.getValue());
            reservationDto.setEndDate(endDate.getValue());
            reservationDto.setCarId(selectedCar.getId());
            reservationDto.setUserId(selectedUser.getId());
            reservationDto.setOptionIds(selectedOptions.stream().map(Option::getId).collect(Collectors.toList()));

            Reservation reservation = reservationMapper.mapToReservation(reservationDto);
            reservationService.addReservation(reservation);
            Notification.show("Reservation created successfully");
            dialog.close();

            refreshGrid();
        });

        formLayout.add(startDate, endDate, carComboBox, userComboBox, optionsComboBox, saveButton);
        dialog.add(formLayout);
        dialog.open();
    }

    private void refreshGrid() {
        List<ReservationDto> reservations = reservationMapper.mapToReservationDtoList(reservationService.findAll());
        reservationGrid.setItems(reservations);
    }
}
