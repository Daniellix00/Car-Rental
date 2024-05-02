package com.crud.rental.domainTests;

import com.crud.rental.domain.User;
import com.crud.rental.exception.UserNotFoundException;
import com.crud.rental.repository.UserRepository;
import com.crud.rental.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class UserTestSuite {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @BeforeEach
    public void cleanUp(){
        userRepository.deleteAll();
        assertTrue(userRepository.findAll().isEmpty());
    }
    @AfterEach
    public void setUp(){
        userRepository.deleteAll();
    }
    @Test
    public void testGetAllUsers() {
        //Given
        User user1 = new User("John Doe", "john_doe");
        User user2 = new User("Jane Smith", "jane_smith");
        userService.createUser(user1);
        userService.createUser(user2);
        //Then
        assertFalse(userService.getAllUsers().isEmpty());
    }
    @Test
    public void testGetUserById() throws UserNotFoundException {
        //Given
        User user1 = new User("John Doe", "john_doe");
        User user2 = new User("Jane Smith", "jane_smith");
        userService.createUser(user1);
        userService.createUser(user2);
        //When
        long userId = userService.getAllUsers().get(0).getId();
        //Then
        assertNotNull(userService.getUserById(userId));
    }
    @Test
    public void testCreateUser() {
    //Given
    User newUser = new User("Test User", "test_user");
    //When
    userService.createUser(newUser);
    //Then
    assertFalse(userService.getAllUsers().isEmpty());
    }
    @Test
    public void testDeleteUser() throws UserNotFoundException{
     //Given
        User user1 = new User("John Doe", "john_doe");
        User user2 = new User("Jane Smith", "jane_smith");
        userService.createUser(user1);
        userService.createUser(user2);
    //When
        userService.deleteUser(user1.getId());
    //Then
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(user1.getId()));
    }
}
