package com.crud.rental.repository;

import com.crud.rental.domain.FuelUsage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuelUsageRepository extends JpaRepository<FuelUsage, Long> {
}
