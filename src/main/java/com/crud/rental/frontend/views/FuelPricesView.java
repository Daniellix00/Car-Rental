package com.crud.rental.frontend.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Route("fuel-prices")
public class FuelPricesView extends VerticalLayout {

    public FuelPricesView() {
        List<FuelPrice> fuelPrices = Arrays.asList(
                new FuelPrice("Warsaw", "95", BigDecimal.valueOf(5.50)),
                new FuelPrice("Krakow", "95", BigDecimal.valueOf(5.40)),
                new FuelPrice("Gdansk", "95", BigDecimal.valueOf(5.45))
        );

        Grid<FuelPrice> grid = new Grid<>(FuelPrice.class);
        grid.setItems(fuelPrices);

        add(grid);
    }

    public static class FuelPrice {
        private String region;
        private String fuelType;
        private BigDecimal price;

        public FuelPrice(String region, String fuelType, BigDecimal price) {
            this.region = region;
            this.fuelType = fuelType;
            this.price = price;
        }

        public String getRegion() {
            return region;
        }

        public String getFuelType() {
            return fuelType;
        }

        public BigDecimal getPrice() {
            return price;
        }
    }
}
