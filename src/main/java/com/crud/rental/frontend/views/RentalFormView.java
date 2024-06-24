package com.crud.rental.frontend.views;

import com.crud.rental.domain.Car;
import com.crud.rental.domain.Reservation;
import com.crud.rental.domain.ReservationDto;
import com.crud.rental.mapper.ReservationMapper;
import com.crud.rental.service.CarService;
import com.crud.rental.service.ReservationService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("rental-form")
public class RentalFormView extends VerticalLayout {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private CarService carService;

    public RentalFormView(ReservationService reservationService, ReservationMapper reservationMapper, CarService carService) {
        this.reservationService = reservationService;
        this.reservationMapper = reservationMapper;
        this.carService = carService;

        // Dodanie przycisku do tworzenia rezerwacji
        Button createReservationButton = new Button("Create Reservation", e -> openReservationDialog());
        add(createReservationButton);
    }

    // Otwarcie dialogu do tworzenia nowej rezerwacji
    private void openReservationDialog() {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        DatePicker startDate = new DatePicker("Start Date");
        DatePicker endDate = new DatePicker("End Date");
        ComboBox<Car> carComboBox = new ComboBox<>("Car");
        carComboBox.setItemLabelGenerator(Car::getCarBrand); // lub inna metoda do wyświetlania szczegółów samochodu
        carComboBox.setItems(carService.getAvailableCars()); // zakładamy, że getAvailableCars zwraca listę dostępnych samochodów

        NumberField userId = new NumberField("User ID");

        // Przycisk do zapisywania rezerwacji
        Button saveButton = new Button("Save", e -> {
            Car selectedCar = carComboBox.getValue();
            if (selectedCar == null) {
                Notification.show("Please select a car");
                return;
            }

            ReservationDto reservationDto = new ReservationDto();
            reservationDto.setStartDate(startDate.getValue());
            reservationDto.setEndDate(endDate.getValue());
            reservationDto.setId(selectedCar.getId());
            //reservationDto.setUserId(userId.getValue().longValue());

            Reservation reservation = reservationMapper.mapToReservation(reservationDto);
            reservationService.addReservation(reservation);
            Notification.show("Reservation created successfully");
            dialog.close();
        });

        formLayout.add(startDate, endDate, carComboBox, userId, saveButton);
        dialog.add(formLayout);
        dialog.open();
    }
}
