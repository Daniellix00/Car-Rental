package com.crud.rental.repository;

import com.crud.rental.domain.Damage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DamageRespository  extends JpaRepository<Damage, Long> {
    Optional<Damage>findById(Long damageId);
}
