package com.crud.rental.domainTests;

import com.crud.rental.domain.Damage;
import com.crud.rental.repository.DamageRespository;
import com.crud.rental.service.DamageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DamageTestSuite {
    @Autowired
    private DamageService damageService;
    @Autowired
    private DamageRespository damageRespository;
    @AfterEach
    public void cleanUp() {
       damageRespository.deleteAll();
       assertTrue(damageRespository.findAll().isEmpty());
    }
    @BeforeEach
    public void setUp(){
       damageRespository.deleteAll();
    }
    @Test
    public void testSaveAndGetAllDamages() {
        // Given
        Damage damage1 = new Damage(new BigDecimal("200.00"));
        damage1.setDescription("Damage description 1");

        Damage damage2 = new Damage(new BigDecimal("150.00"));
        damage2.setDescription("Damage description 2");

        // When
        damageService.saveDamage(damage1);
        damageService.saveDamage(damage2);

        // Then
        List<Damage> allDamages = damageService.getAllDamages();
        assertNotNull(allDamages);
        assertEquals(2, allDamages.size());
        assertTrue(allDamages.contains(damage1));
        assertTrue(allDamages.contains(damage2));
    }

    @Test
    public void testDeleteDamageById() {
        // Given
        Damage damage = new Damage(new BigDecimal("200.00"));
        damage.setDescription("Damage description");

        // When
        damageService.saveDamage(damage);
        List<Damage> allDamagesBeforeDelete = damageService.getAllDamages();
        assertNotNull(allDamagesBeforeDelete);
        assertEquals(1, allDamagesBeforeDelete.size());

        damageService.delateDamegeById(damage.getId());

        // Then
        List<Damage> allDamagesAfterDelete = damageService.getAllDamages();
        assertNotNull(allDamagesAfterDelete);
        assertEquals(0, allDamagesAfterDelete.size());
    }

}
