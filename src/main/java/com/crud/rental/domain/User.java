package com.crud.rental.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "USERS")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "USERNAME", nullable = false)
    private String username;
    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations;

    public User(String name, String username) {
        this.name = name;
        this.username = username;
    }
}
