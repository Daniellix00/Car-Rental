package com.crud.rental.frontend.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("")
public class MainView extends VerticalLayout {

    public MainView() {
        // Header
        H1 header = new H1("Welcome to Car Rental Service");

        // Description
        Paragraph description = new Paragraph(
                "Welcome to our Car Rental Service. We offer a wide range of vehicles for all your needs. " +
                        "Whether you're looking for a compact car for city driving or a spacious SUV for a family trip, " +
                        "we've got you covered. Check out our fleet, find out the latest fuel prices, and book your rental car today!"
        );

        // Links to other pages
        RouterLink carsLink = new RouterLink("Browse Our Cars", CarsView.class);
        RouterLink fuelPricesLink = new RouterLink("Fuel Prices", FuelPricesView.class);
        RouterLink rentalFormLink = new RouterLink("Rental Form", RentalFormView.class);
        RouterLink fuelUsageLink = new RouterLink("End Rental and Add Fuel Usage", FuelUsageFormView.class);
        RouterLink userListLink = new RouterLink("User List", UserListView.class);

        HorizontalLayout linksLayout = new HorizontalLayout(carsLink, fuelPricesLink, rentalFormLink, fuelUsageLink, userListLink);
        linksLayout.addClassName("links-layout");

        // Adding components to the layout
        add(header, description, linksLayout);

        // Styling
        addClassName("main-view");
    }
}
