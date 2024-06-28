package com.crud.rental.repository;

import com.crud.rental.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
Optional<User>findById(Long userId);
    User findByUsername(String username);
}
