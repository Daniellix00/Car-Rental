package com.crud.rental.frontend.views;

import com.crud.rental.domain.FuelUsage;
import com.crud.rental.domain.Reservation;
import com.crud.rental.service.FuelUsageService;
import com.crud.rental.service.ReservationService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
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

@Route("fuel-usage")
public class FuelUsageFormView extends VerticalLayout {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private FuelUsageService fuelUsageService;

    private Grid<Reservation> reservationGrid = new Grid<>(Reservation.class);

    public FuelUsageFormView(ReservationService reservationService, FuelUsageService fuelUsageService) {
        this.reservationService = reservationService;
        this.fuelUsageService = fuelUsageService;

        // Dodanie przycisku do zakończenia wynajmu i dodania zużycia paliwa
        add(new Button("End Rental and Add Fuel Usage", e -> openFuelUsageDialog()));

        // Konfiguracja siatki
        configureGrid();
        add(reservationGrid);

        // Odświeżenie danych w siatce
        refreshGrid();
    }

    // Konfiguracja siatki
    private void configureGrid() {
        reservationGrid.setColumns("id", "startDate", "endDate", "totalPrice", "status");
        reservationGrid.addComponentColumn(reservation -> new Button("End Rental", e -> openFuelUsageDialog(reservation)));
    }

    // Otwarcie dialogu do dodania zużycia paliwa (bez wstępnie wybranej rezerwacji)
    private void openFuelUsageDialog() {
        openFuelUsageDialog(null);
    }

    // Otwarcie dialogu do dodania zużycia paliwa (z wstępnie wybraną rezerwacją)
    private void openFuelUsageDialog(Reservation reservation) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        ComboBox<Reservation> reservationComboBox = new ComboBox<>("Reservation");
        reservationComboBox.setItemLabelGenerator(res -> "ID: " + res.getId() + ", Car: " + res.getCar().getCarBrand());
        reservationComboBox.setItems(reservationService.getAllReservations());

        if (reservation != null) {
            reservationComboBox.setValue(reservation);
        }

        NumberField startKm = new NumberField("Start Kilometers");
        NumberField endKm = new NumberField("End Kilometers");
        NumberField fuelPrice = new NumberField("Fuel Price per Liter");
        NumberField totalCost = new NumberField("Total Cost");
        NumberField fuelConsumption = new NumberField("Fuel Consumption");
        TextField fuelType = new TextField("Fuel Type");

        // Przycisk do zapisywania zużycia paliwa
        Button saveButton = new Button("Save", e -> {
            Reservation selectedReservation = reservationComboBox.getValue();
            if (selectedReservation == null) {
                Notification.show("Please select a reservation");
                return;
            }

            FuelUsage fuelUsage = new FuelUsage();
            fuelUsage.setStartKm(startKm.getValue().intValue());
            fuelUsage.setEndKm(endKm.getValue().intValue());
            fuelUsage.setFuelPrice(BigDecimal.valueOf(fuelPrice.getValue()));
            fuelUsage.setTotalCost(BigDecimal.valueOf(totalCost.getValue()));
            fuelUsage.setFuelConsumption(BigDecimal.valueOf(fuelConsumption.getValue()));
            fuelUsage.setFuelType(fuelType.getValue());
            fuelUsage.setReservation(selectedReservation);

            fuelUsageService.saveFuelUsage(fuelUsage);
            Notification.show("Fuel usage added successfully");
            dialog.close();
        });

        formLayout.add(reservationComboBox, startKm, endKm, fuelPrice, totalCost, fuelConsumption, fuelType, saveButton);
        dialog.add(formLayout);
        dialog.open();
    }

    // Odświeżenie danych w siatce
    private void refreshGrid() {
        List<Reservation> reservations = reservationService.getAllReservations();
        reservationGrid.setItems(reservations);
    }
}
