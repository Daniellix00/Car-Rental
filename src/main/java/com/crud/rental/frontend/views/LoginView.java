package com.crud.rental.frontend.views;

import com.crud.rental.domain.User;
import com.crud.rental.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("login")
public class LoginView extends VerticalLayout {

    private final UserService userService;

    public LoginView(UserService userService) {
        this.userService = userService;

        TextField usernameField = new TextField("Username");
        PasswordField passwordField = new PasswordField("Password");
        Button loginButton = new Button("Login", event -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();
            User user = userService.findByUsername(username);

            if (user != null && user.getPassword().equals(password)) {
                VaadinSession.getCurrent().setAttribute(User.class, user);
                getUI().ifPresent(ui -> ui.navigate("")); // Redirect to home or dashboard
            } else {
                Notification.show("Invalid username or password");
            }
        });

        Button registerButton = new Button("Register", event -> {
            getUI().ifPresent(ui -> ui.navigate("register"));
        });

        HorizontalLayout buttonsLayout = new HorizontalLayout(loginButton, registerButton);

        add(usernameField, passwordField, buttonsLayout);
    }
}
