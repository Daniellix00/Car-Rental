package com.crud.rental.seeder;

import com.crud.rental.domain.Option;
import com.crud.rental.domain.User;
import com.crud.rental.repository.OptionRepository;
import com.crud.rental.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner initDatabase(OptionRepository optionRepository, UserRepository userRepository) {
        return args -> {
            if (optionRepository.count() == 0) {
                List<Option> options = List.of(
                        new Option(0, "GPS", new BigDecimal("5.00"), null),
                        new Option(0, "Child Seat", new BigDecimal("7.50"), null),
                        new Option(0, "Additional Driver", new BigDecimal("10.00"), null)
                );
                optionRepository.saveAll(options);
            }

            if (userRepository.count() == 0) {
                User adminUser = new User(0L, "admin", "admin", "Admin", "User", true, null);
                userRepository.save(adminUser);
            }
        };
    }
}
