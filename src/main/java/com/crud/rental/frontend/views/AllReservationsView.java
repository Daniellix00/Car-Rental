package com.crud.rental.frontend.views;

import com.crud.rental.domain.Car;
import com.crud.rental.domain.Reservation;
import com.crud.rental.domain.ReservationDto;
import com.crud.rental.mapper.ReservationMapper;
import com.crud.rental.service.CarService;
import com.crud.rental.service.ReservationService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Route("reservations")
public class AllReservationsView extends VerticalLayout {

    private final ReservationService reservationService;
    private final CarService carService;
    private final ReservationMapper reservationMapper;

    public AllReservationsView(ReservationService reservationService, ReservationMapper reservationMapper, CarService carService) {
        this.reservationService = reservationService;
        this.reservationMapper = reservationMapper;
        this.carService = carService;

        Grid<ReservationDto> grid = new Grid<>(ReservationDto.class);
        grid.setItems(reservationMapper.mapToReservationDtoList(reservationService.findAll()));

        grid.addColumn(ReservationDto::getId).setHeader("ID");
        grid.addColumn(ReservationDto::getStartDate).setHeader("Start Date");
        grid.addColumn(ReservationDto::getEndDate).setHeader("End Date");
        grid.addColumn(ReservationDto::getTotalPrice).setHeader("Total Price");
        grid.addColumn(reservation -> String.join(", ", reservation.getOptionNames())).setHeader("Options");

        grid.addComponentColumn(reservation -> {
            Button endButton = new Button("End Reservation", event -> openEndReservationDialog(reservation));
            return endButton;
        }).setHeader("Actions");

        add(grid);
    }

    private void openEndReservationDialog(ReservationDto reservationDto) {
        Dialog dialog = new Dialog();
        TextField mileageField = new TextField("Mileage");
        ComboBox<String> regionComboBox = new ComboBox<>("Region");

        regionComboBox.setItems(fetchRegions());

        Button saveButton = new Button("Save", event -> {
            String mileage = mileageField.getValue();
            String region = regionComboBox.getValue();

            if (mileage.isEmpty() || region == null) {
                Notification.show("All fields are required");
                return;
            }

            try {
                int newMileage = Integer.parseInt(mileage);
                Reservation reservation = reservationService.findById(reservationDto.getId());
                int mileageDiff = newMileage - reservation.getMileage();
                long days = ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());
                BigDecimal fuelPrice = getFuelPrice(region, reservation.getCar().getFuel());

                BigDecimal totalPrice = reservation.getCar().getPrice();

                totalPrice = totalPrice.multiply(BigDecimal.valueOf(days));
                totalPrice = totalPrice.add(fuelPrice.multiply(BigDecimal.valueOf(mileageDiff)));

                reservation.setMileage(newMileage);
                reservation.setTotalPrice(totalPrice);
                reservation.setEnded(true);

                reservationService.updateReservation(reservation);

                Car car = reservation.getCar();
                car.setKilometers(newMileage);
                carService.saveCar(car);

                Notification.show("Reservation ended successfully. Total price: " + totalPrice);
                dialog.close();
            } catch (NumberFormatException ex) {
                Notification.show("Mileage must be a number");
            }
        });

        dialog.add(mileageField, regionComboBox, saveButton);
        dialog.open();
    }

    private BigDecimal getFuelPrice(String region, String fuelType) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/fuels/prices";
        ResponseEntity<List<FuelPriceDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<FuelPriceDto>>() {}
        );
        List<FuelPriceDto> fuelPrices = response.getBody();
        return fuelPrices.stream()
                .filter(fuelPrice -> fuelPrice.getRegion().equals(region) && fuelPrice.getFuelType().equals(fuelType))
                .map(FuelPriceDto::getPrice)
                .findFirst()
                .orElse(BigDecimal.ZERO); // default to 0 if no price is found
    }

    private List<String> fetchRegions() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/fuels/prices"; // Adjust the URL as needed
        ResponseEntity<List<FuelPriceDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<FuelPriceDto>>() {}
        );
        List<FuelPriceDto> fuelPrices = response.getBody();
        return fuelPrices.stream()
                .map(FuelPriceDto::getRegion)
                .distinct()
                .collect(Collectors.toList());
    }

    public static class FuelPriceDto {
        private String region;
        private String fuelType;
        private BigDecimal price;

        // Getters and setters

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getFuelType() {
            return fuelType;
        }

        public void setFuelType(String fuelType) {
            this.fuelType = fuelType;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }
    }
}
