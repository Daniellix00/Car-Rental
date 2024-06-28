package com.crud.rental.repository;

import com.crud.rental.domain.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OptionRepository extends JpaRepository<Option,Long> {
    Optional<Option>findById(Long optionId);
    Optional<Option>findByName(String name);
}
