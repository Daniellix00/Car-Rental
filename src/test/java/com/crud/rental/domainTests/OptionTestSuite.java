package com.crud.rental.domainTests;

import com.crud.rental.domain.Option;
import com.crud.rental.exception.OptionNotFoundException;
import com.crud.rental.repository.OptionRepository;
import com.crud.rental.service.OptionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OptionTestSuite {
    @Autowired
    private OptionService optionService;
    @Autowired
    private OptionRepository optionRepository;
    @AfterEach
    public void cleanUp(){
     optionRepository.deleteAll();
     assertTrue(optionRepository.findAll().isEmpty());
    }
    @BeforeEach
    public void setUp(){
        optionRepository.deleteAll();
    }
    @Test
    public void testAddAndGetOption() {
        // Given
        Option option = new Option();
        option.setName("Air Conditioning");
        option.setPrice(BigDecimal.valueOf(200));


        // When
        optionService.addOption(option);
        Option savedOption = optionService.getOptionById(option.getId());
        // Then
        assertNotNull(savedOption);
        assertEquals(option.getName(), savedOption.getName());
        assertTrue(option.getPrice().compareTo(savedOption.getPrice()) == 0, "Prices should be equal");

    }
    @Test
    public void testDeleteOption() throws OptionNotFoundException {
        // Given
        Option option = new Option();
        option.setName("Air Conditioning");
        option.setPrice(BigDecimal.valueOf(200));
        // When
        optionService.addOption(option);
        optionService.deleteOption(option.getId());
        // Then
        assertThrows(OptionNotFoundException.class, () -> optionService.getOptionById(option.getId()));
    }
    @Test
    public void testGetAllOptions() {
        // Given
        Option option1 = new Option();
        option1.setName("Bluetooth Connectivity");
        option1.setPrice(BigDecimal.valueOf(200));
        Option option2 = new Option();
        option2.setName("Parking Sensors");
        option2.setPrice(BigDecimal.valueOf(450));

        // When
        optionService.addOption(option1);
        optionService.addOption(option2);

        // Then
        assertEquals(2, optionService.getAllOptions().size());
    }

    }

