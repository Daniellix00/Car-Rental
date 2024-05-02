package com.crud.rental.domainTests;

import com.crud.rental.domain.Car;
import com.crud.rental.domain.Damage;
import com.crud.rental.domain.Option;
import com.crud.rental.domain.Reservation;
import com.crud.rental.exception.OptionNotFoundException;
import com.crud.rental.exception.ReservationNotFoundException;
import com.crud.rental.repository.CarRepository;
import com.crud.rental.repository.DamageRespository;
import com.crud.rental.repository.OptionRepository;
import com.crud.rental.repository.ReservationRepository;
import com.crud.rental.service.CarService;
import com.crud.rental.service.OptionService;
import com.crud.rental.service.ReservationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import static com.helger.commons.mock.CommonsAssert.assertEquals;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

public class ReservationTestSuite {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private OptionService optionService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private CarService carService;
    @Autowired
    private DamageRespository damageRepository;
    @AfterEach
    public void cleanup() {
        reservationRepository.deleteAll();
        carRepository.deleteAll();
        optionRepository.deleteAll();
        damageRepository.deleteAll();
        assertTrue(carRepository.findAll().isEmpty());
        assertTrue(reservationRepository.findAll().isEmpty());
        assertTrue(optionRepository.findAll().isEmpty());
        assertTrue(damageRepository.findAll().isEmpty());
    }
    @BeforeEach
    public void setup() {
        reservationRepository.deleteAll();
        carRepository.deleteAll();
        optionRepository.deleteAll();
        damageRepository.deleteAll();
    }

    @Test
    public void testAddReservation() {
        //Given
        Reservation reservation = new Reservation();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(2);
        BigDecimal totalPrice = BigDecimal.valueOf(150.0);
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setTotalPrice(totalPrice);
        reservation.setStatus(true);
        //When
       Reservation savedReservation = reservationRepository.save(reservation);
        //Then
        assertNotNull(savedReservation.getId());
        assertEquals(totalPrice, savedReservation.getTotalPrice()); // Porównanie obiektów BigDecimal
        assertEquals(startDate, savedReservation.getStartDate());
        assertEquals(endDate, savedReservation.getEndDate());
    }
    @Test
    public void testFindReservationById() {
        // Given
        Reservation reservation = new Reservation();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(2);
        BigDecimal totalPrice = BigDecimal.valueOf(150.0); // Użycie BigDecimal
        reservation.setStatus(true);
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setTotalPrice(totalPrice);
        Reservation savedReservation = reservationRepository.save(reservation);

        // When
        Reservation retrievedReservation = reservationRepository.findById(savedReservation.getId()).orElse(null);

        // Then
        assertNotNull(retrievedReservation);
        assertEquals(savedReservation.getId(), retrievedReservation.getId());
        assertEquals(startDate, retrievedReservation.getStartDate());
        assertEquals(endDate, retrievedReservation.getEndDate());
    }
    @Test
    public void testAddCarToDatabase() {
        // Given
        Car car = new Car();
        car.setColour("Red");
        car.setCarBrand("Toyota");
        car.setPrice(BigDecimal.valueOf(25000));
        car.setAvailability(true);
        car.setFuel("Petrol");
        car.setFuelCapacity(50);

        // When
        Car savedCar = carRepository.save(car);

        // Then
        assertNotNull(savedCar.getId());
        assertEquals("Red", savedCar.getColour());
        assertEquals("Toyota", savedCar.getCarBrand());
        assertEquals(BigDecimal.valueOf(25000), savedCar.getPrice());
        assertTrue(savedCar.isAvailability());
        assertEquals("Petrol", savedCar.getFuel());
        assertEquals(50, savedCar.getFuelCapacity());
    }
    @Test
    public void testAddCarToReservation()  {
        // Given
        Reservation reservation = new Reservation();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(2);
        BigDecimal totalPrice = BigDecimal.valueOf(150.0); // Użycie BigDecimal
        reservation.setStatus(true);
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setTotalPrice(totalPrice);
        reservationRepository.save(reservation);

        Car car = new Car();
        car.setColour("Red");
        car.setCarBrand("Toyota");
        car.setPrice(BigDecimal.valueOf(25000));
        car.setAvailability(true);
        car.setFuel("Petrol");
        car.setFuelCapacity(50);

        Car savedCar = carService.saveCar(car); // Zapisujemy auto i pobieramy zapisane auto

        // When
        Reservation updatedReservation = reservationService.addCarToReservation(savedCar.getId(), reservation.getId());
        reservationRepository.save(updatedReservation);

        // Then
        assertNotNull(updatedReservation);
        assertNotNull(updatedReservation.getId());
        assertNotNull(updatedReservation.getCar());
        assertTrue(updatedReservation.getCar().getId() == savedCar.getId());
    }
    @Test
    public void testDeleteCarFromReservation()  {
        // Given
        Reservation reservation = new Reservation();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(2);
        BigDecimal totalPrice = BigDecimal.valueOf(150.0); // Użycie BigDecimal
        reservation.setStatus(true);
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setTotalPrice(totalPrice);
        reservationRepository.save(reservation);

        Car car = new Car();
        car.setColour("Red");
        car.setCarBrand("Toyota");
        car.setPrice(BigDecimal.valueOf(25000));
        car.setAvailability(true);
        car.setFuel("Petrol");
        car.setFuelCapacity(50);
        carService.saveCar(car);
        reservationService.addCarToReservation(car.getId(), reservation.getId());

        // When
        Reservation updatedReservation = reservationService.deleteCarFromReservation(reservation.getId(),car.getId());

        // Save the updated reservation to database
        reservationRepository.save(updatedReservation);

        // Then
        assertNotNull(updatedReservation);
        assertNotNull(updatedReservation.getId());
        assertNull(updatedReservation.getCar());
    }
    @Test
    public void testDeleteCarFromDatabase() {
        // Given
        Car car = new Car();
        car.setColour("Red");
        car.setCarBrand("Toyota");
        car.setPrice(BigDecimal.valueOf(25000));
        car.setAvailability(true);
        car.setFuel("Petrol");
        car.setFuelCapacity(50.0); // Double value

        Car savedCar = carRepository.save(car);

        // When
        carRepository.delete(savedCar);

        // Then
        assertFalse(carRepository.existsById(savedCar.getId()));
    }

    @Test
    public void testSaveOptionToDatabase() {
        // Given
        Option option = new Option();
        option.setName("Test Option");
        option.setPrice(BigDecimal.valueOf(10.00));

        // When
        Option savedOption = optionRepository.save(option);

        // Then
        assertNotNull(savedOption);
        assertNotNull(savedOption.getId()); // Potwierdzamy, że opcja ma przypisane ID
        assertTrue(optionRepository.findById(savedOption.getId()).isPresent()); // Potwierdzamy, że opcja jest w bazie danych
    }
    @Test
    public void testAddOptionToReservation()  {
        // Given
        Reservation reservation = new Reservation();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(2);
        BigDecimal totalPrice = BigDecimal.valueOf(150.0); // Użycie BigDecimal
        reservation.setStatus(true);
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setTotalPrice(totalPrice);

         reservationRepository.save(reservation);


        Option option = new Option();
        option.setName("Test Option");
        option.setPrice(BigDecimal.valueOf(10.00));
        optionRepository.save(option);

        // When
        Reservation updatedReservation = reservationService.addOptionToReservation(reservation.getId(), option.getId());

        // Then
        assertNotNull(updatedReservation);
        assertNotNull(updatedReservation.getOptions());
        assertEquals(1, updatedReservation.getOptions().size());
    }
    @Test
    public void testDeleteOptionFromReservation() throws ReservationNotFoundException, OptionNotFoundException {
        // Given
        Reservation reservation = new Reservation();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(2);
        BigDecimal totalPrice = BigDecimal.valueOf(150.0); // Użycie BigDecimal
        reservation.setStatus(true);
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setTotalPrice(totalPrice);
        reservationRepository.save(reservation);

        Option option = new Option();
        option.setName("Test Option");
        option.setPrice(BigDecimal.valueOf(10.00));
        optionRepository.save(option);

        reservationService.addOptionToReservation(reservation.getId(), option.getId());

        // When
        reservationService.deleteOptionFromReservation(reservation.getId(), option.getId());
        Reservation updatedReservation = reservationRepository.findById(reservation.getId())
                .orElseThrow(ReservationNotFoundException::new);

        // Then
        assertNotNull(updatedReservation);
        assertNotNull(updatedReservation.getOptions());
        assertEquals(0, updatedReservation.getOptions().size());

    }
@Test
    public void delateOptionToDatabase(){
    //Given
    Option option = new Option();
    option.setName("Test Option");
    option.setPrice(BigDecimal.valueOf(10.00));
   Option savedOption = optionRepository.save(option);
    //When
    optionRepository.delete(option);
    //Then
    assertFalse(carRepository.existsById(savedOption.getId()));
}
    @Test
    public void testSaveDamageToDatabase() {
        // Given
        Damage damage = new Damage();
        damage.setDescription("Scratch on the door");
        damage.setRepairCost(new BigDecimal("100.00"));

        // When
        Damage savedDamage = damageRepository.save(damage);

        // Then
        assertNotNull(savedDamage.getId());
    }
@Test
@Transactional
public void testAddDamageToReservation() throws ReservationNotFoundException {
    // Given
    Reservation reservation = new Reservation();
    reservation.setStartDate(LocalDate.now());
    reservation.setEndDate(LocalDate.now().plusDays(3));
    reservation.setTotalPrice(BigDecimal.ZERO);
    reservation.setStatus(true);
    reservationService.addReservation(reservation);

    Damage damage = new Damage();
    damage.setDescription("Scratch on the door");
    damage.setRepairCost(new BigDecimal("100.00"));
    damage = damageRepository.save(damage);

    // When
    reservationService.addDamageToReservation(reservation.getId(), damage.getId());

    // Then
    Reservation updatedReservation = reservationService.getReservationById(reservation.getId());
    assertTrue(updatedReservation.getDamages().contains(damage));
}

@Test
@Transactional
    public void testCalculateDamageCosts() {

    Reservation reservation = new Reservation();
    reservation.setStartDate(LocalDate.now());
    reservation.setEndDate(LocalDate.now().plusDays(3));
    reservation.setTotalPrice(BigDecimal.ZERO);
    reservation.setStatus(true);
        reservationService.addReservation(reservation);
    Damage damage1 = new Damage(new BigDecimal("200.00"));
    damage1.setDescription("Damage description 1");
    damage1 = damageRepository.save(damage1);
    Damage damage2 = new Damage(new BigDecimal("150.00"));
    damage2.setDescription("Damage description 2");
    damage2 = damageRepository.save(damage2);
    reservationService.addDamageToReservation(reservation.getId(), damage1.getId());
    reservationService.addDamageToReservation(reservation.getId(), damage2.getId());
        BigDecimal expected = new BigDecimal("350.00");
        BigDecimal result = reservationService.calculateDamageCosts(reservation);

        assertEquals(expected, result);
    }
    @Test
    public void testCalculateOptionCosts() throws ReservationNotFoundException, OptionNotFoundException {
        //Given
        Reservation reservation = new Reservation();
        reservation.setTotalPrice(BigDecimal.ZERO);
        reservation.setStartDate(LocalDate.now()); // Ustawiamy datę początkową
        reservation.setEndDate(LocalDate.now().plusDays(3)); // Ustawiamy datę końcową
        reservation.setStatus(true); // Ustawiamy status
        reservationService.addReservation(reservation);
        Option option1 = new Option();
        option1.setName("Option 1");
        option1.setPrice(new BigDecimal("100.00"));
         optionService.addOption(option1);
         Option option2 = new Option();
        option2.setName("Option 2");
        option2.setPrice(new BigDecimal("50.00"));
        optionService.addOption(option2);
        Reservation updateReservation = reservationService.addOptionToReservation(reservation.getId(), option1.getId());
    updateReservation = reservationService.addOptionToReservation(reservation.getId(), option2.getId());
        reservationService.addReservation(updateReservation);
        BigDecimal expected = new BigDecimal("150.00");
        //When
        BigDecimal result = reservationService.calculateOptionCosts(updateReservation);
        //Then
        assertEquals(expected,result);
    }
    @Test
    public void testCalculateBasePrice() {
        // Given
        Car car = new Car();
        car.setPrice(BigDecimal.valueOf(100));

        LocalDate startDate = LocalDate.of(2024, 4, 30);
        LocalDate endDate = LocalDate.of(2024, 5, 5);

        Reservation reservation = new Reservation();
        reservation.setCar(car);
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);

        // When
        BigDecimal basePrice = reservationService.calculateBasePrice(reservation);

        // Then
        BigDecimal expectedPrice = BigDecimal.valueOf(500); // Cena za 5 dni wynosi 500 jednostek waluty
        assertEquals(expectedPrice, basePrice);
    }
    @Test
    @Transactional
    public void testCalculateTotalPrice() throws ReservationNotFoundException, OptionNotFoundException {
        // Tworzymy samochód
        Car car = new Car();
        car.setColour("Red");
        car.setCarBrand("Toyota");
        car.setPrice(BigDecimal.valueOf(100));
        car.setKilometers(50000);
        car.setAvailability(true);
        car.setFuel("Gasoline");
        car.setFuelCapacity(50.0);
        carService.saveCar(car);
        //Rezerwacja
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now());
        reservation.setEndDate(LocalDate.now().plusDays(3));
        reservation.setStatus(true);
        reservation.setTotalPrice(BigDecimal.ZERO);
        reservation.setCar(car);
        reservationService.addReservation(reservation);

        // Dodajemy uszkodzenia
        Damage damage1 = new Damage(new BigDecimal("200.00"));
        damage1.setDescription("Damage description 1");
        damage1 = damageRepository.save(damage1);
        Damage damage2 = new Damage(new BigDecimal("150.00"));
        damage2.setDescription("Damage description 2");
        damage2 = damageRepository.save(damage2);
         reservationService.addDamageToReservation(reservation.getId(), damage1.getId());
        reservationService.addDamageToReservation(reservation.getId(), damage2.getId());

        // Dodajemy opcje
        Option option1 = new Option();
        option1.setName("Option 1");
        option1.setPrice(new BigDecimal("100.00"));
        optionService.addOption(option1);
        Option option2 = new Option();
        option2.setName("Option 2");
        option2.setPrice(new BigDecimal("50.00"));
        optionService.addOption(option2);
        reservationService.addOptionToReservation(reservation.getId(), option1.getId());
        reservationService.addOptionToReservation(reservation.getId(), option2.getId());

        // Obliczamy oczekiwaną cenę
        BigDecimal basePrice = BigDecimal.valueOf(300); // Przykładowa cena bazowa
        BigDecimal damageCosts = new BigDecimal("350.00"); // Wyliczona suma uszkodzeń
        BigDecimal optionCosts = new BigDecimal("150.00"); // Wyliczona suma opcji
        BigDecimal expectedTotalPrice =  basePrice.add(damageCosts).add(optionCosts);

        // Wywołujemy metodę calculateTotalPrice
        BigDecimal totalPrice = reservationService.calculateTotalPrice(reservation.getId());

        // Sprawdzamy czy obliczona cena jest zgodna z oczekiwaną
        assertEquals(expectedTotalPrice, totalPrice);
    }



}
