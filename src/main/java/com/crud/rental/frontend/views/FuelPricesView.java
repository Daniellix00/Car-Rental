package com.crud.rental.frontend.views;

import com.crud.rental.domain.FuelPriceDto;
import com.crud.rental.service.FuelUsageService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Route("fuel-prices")
public class FuelPricesView extends VerticalLayout {

    @Autowired
    private FuelUsageService fuelService;

    private Grid<RegionFuelPrices> grid = new Grid<>(RegionFuelPrices.class);

    public FuelPricesView(FuelUsageService fuelService) {
        this.fuelService = fuelService;

        // Dodanie siatki do widoku
        add(grid);

        // Odświeżenie danych w siatce
        refreshGrid();
    }

    // Metoda odświeżająca dane w siatce
    private void refreshGrid() {
        try {
            List<FuelPriceDto> fuelPrices = fuelService.getFuelPrices();

            // Grupowanie danych według regionu
            Map<String, Map<String, BigDecimal>> regionFuelPriceMap = fuelPrices.stream()
                    .collect(Collectors.groupingBy(
                            FuelPriceDto::getRegion,
                            Collectors.toMap(FuelPriceDto::getFuelType, FuelPriceDto::getPrice)
                    ));

            // Konwersja danych na obiekty RegionFuelPrices dla siatki
            List<RegionFuelPrices> regionFuelPricesList = regionFuelPriceMap.entrySet().stream()
                    .map(entry -> new RegionFuelPrices(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());

            // Ustawienie danych w siatce
            grid.setItems(regionFuelPricesList);

            // Konfiguracja kolumn
            grid.removeAllColumns();
            grid.addColumn(RegionFuelPrices::getRegion).setHeader("Region");

            if (!regionFuelPricesList.isEmpty()) {
                RegionFuelPrices sample = regionFuelPricesList.get(0);
                sample.getFuelPrices().keySet().forEach(fuelType -> {
                    grid.addColumn(rfp -> {
                        BigDecimal price = rfp.getFuelPrices().get(fuelType);
                        return price != null && price.compareTo(BigDecimal.ZERO) != 0 ? price.toString() : "-";
                    }).setHeader(fuelType);
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Wewnętrzna klasa reprezentująca dane dla siatki
    public static class RegionFuelPrices {
        private String region;
        private Map<String, BigDecimal> fuelPrices;

        public RegionFuelPrices(String region, Map<String, BigDecimal> fuelPrices) {
            this.region = region;
            this.fuelPrices = fuelPrices;
        }

        public String getRegion() {
            return region;
        }

        public Map<String, BigDecimal> getFuelPrices() {
            return fuelPrices;
        }
    }
}
