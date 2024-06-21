package com.crud.rental.frontend.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Route("cars")
public class CarsView extends VerticalLayout {

    public CarsView() {
        List<Car> cars = Arrays.asList(
                new Car("Toyota", "Red", BigDecimal.valueOf(100)),
                new Car("BMW", "Blue", BigDecimal.valueOf(150)),
                new Car("Audi", "Black", BigDecimal.valueOf(200))
        );

        Grid<Car> grid = new Grid<>(Car.class);
        grid.setItems(cars);

        add(grid);
    }

    public static class Car {
        private String brand;
        private String color;
        private BigDecimal price;

        public Car(String brand, String color, BigDecimal price) {
            this.brand = brand;
            this.color = color;
            this.price = price;
        }

        public String getBrand() {
            return brand;
        }

        public String getColor() {
            return color;
        }

        public BigDecimal getPrice() {
            return price;
        }
    }
}
