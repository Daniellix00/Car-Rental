package com.crud.rental.frontend.views;

import com.crud.rental.domain.User;
import com.crud.rental.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("register")
public class RegisterView extends VerticalLayout {

    private final UserService userService;

    public RegisterView(UserService userService) {
        this.userService = userService;

        TextField firstNameField = new TextField("First Name");
        TextField lastNameField = new TextField("Last Name");
        TextField usernameField = new TextField("Username");
        PasswordField passwordField = new PasswordField("Password");

        Button registerButton = new Button("Register", event -> {
            String firstName = firstNameField.getValue();
            String lastName = lastNameField.getValue();
            String username = usernameField.getValue();
            String password = passwordField.getValue();

            if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Notification.show("All fields are required");
                return;
            }

            User user = new User(firstName, lastName, username, password);
            userService.createUser(user);
            Notification.show("User registered successfully");
            getUI().ifPresent(ui -> ui.navigate("login"));
        });

        add(firstNameField, lastNameField, usernameField, passwordField, registerButton);
    }
}
