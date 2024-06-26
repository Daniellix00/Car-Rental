package com.crud.rental.frontend.views;

import com.crud.rental.domain.User;
import com.crud.rental.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("users")
public class UserListView extends VerticalLayout {

    @Autowired
    private UserService userService;

    private Grid<User> grid = new Grid<>(User.class);

    public UserListView(UserService userService) {
        this.userService = userService;

        // Dodanie przycisku do dodawania nowego użytkownika
        Button addUserButton = new Button("Add User", e -> openAddUserDialog());
        add(addUserButton);

        // Konfiguracja siatki
        configureGrid();
        add(grid);

        // Odświeżenie danych w siatce
        refreshGrid();
    }

    // Konfiguracja siatki
    private void configureGrid() {
        grid.setColumns("id", "firstName", "lastName");
    }

    // Otwarcie dialogu do dodania nowego użytkownika
    private void openAddUserDialog() {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField firstName = new TextField("First Name");
        TextField lastName = new TextField("Last Name");

        // Przycisk do zapisywania nowego użytkownika
        Button saveButton = new Button("Save", e -> {
            User user = new User();
            user.setFirstName(firstName.getValue());
            user.setLastName(lastName.getValue());

            userService.createUser(user);
            Notification.show("User added successfully");
            refreshGrid(); // Odświeżenie siatki
            dialog.close(); // Zamknięcie dialogu
        });

        formLayout.add(firstName, lastName, saveButton);
        dialog.add(formLayout);
        dialog.open();
    }

    // Odświeżenie danych w siatce
    private void refreshGrid() {
        List<User> users = userService.getAllUsers();
        grid.setItems(users);
    }
}
