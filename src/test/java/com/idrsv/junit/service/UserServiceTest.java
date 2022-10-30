package com.idrsv.junit.service;

import com.idrsv.junit.dto.User;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {
    private UserService userService;


    @BeforeAll
    void init () {
        System.out.println("Before All: ");
    }


    @BeforeEach
    void prepare() {
        userService = new UserService();
        System.out.println("Before each: " + this);
    }

    @Test
    void usersEmptyIfNoUserAdded() {
        System.out.println("Test 1: " + this);
        var users = userService.getAll();
        Assertions.assertTrue(users.isEmpty(), () -> "User list should be empty");
    }

    @Test
    void usersSizeIfUserAdded() {
        System.out.println("Test 2: " + this);
        userService.add(new User());
        userService.add(new User());
        var users = userService.getAll();
        Assertions.assertEquals(2, users.size());
    }

    @AfterEach
    void deleteDaaFromDatabase() {
        System.out.println("After each: " + this);
    }

    @AfterAll
    void closeConnectionPool () {
        System.out.println("After All: ");
    }
}
