package com.crud.rental.frontend.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("rental-form")
public class RentalFormView extends VerticalLayout {

    public RentalFormView() {
        TextField nameField = new TextField("Name");
        DatePicker startDatePicker = new DatePicker("Start Date");
        DatePicker endDatePicker = new DatePicker("End Date");
        Button submitButton = new Button("Submit", e -> Notification.show("Rental Submitted!"));

        add(nameField, startDatePicker, endDatePicker, submitButton);
    }
}
