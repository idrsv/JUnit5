package com.idrsv.junit.service;

import com.idrsv.junit.dto.User;
import org.junit.jupiter.api.*;

import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {
    private static final User IVAN = User.of(1, "Ivan", "123");
    private static final User DANIL = User.of(2, "Danil", "1234");
    private UserService userService;


    @BeforeAll
    void init() {
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
        userService.add(IVAN);
        userService.add(DANIL);
        var users = userService.getAll();
        Assertions.assertEquals(2, users.size());
    }

    @Test
    void loginSuccessIfUserExist() {
        System.out.println("Test 3: " + this);
        userService.add(IVAN);
        Optional<User> userServiceLogin = userService.login(IVAN.getName(), IVAN.getPassword());
        Assertions.assertTrue(userServiceLogin.isPresent());
        userServiceLogin.ifPresent(user -> Assertions.assertEquals(IVAN, user));
    }

    @Test
    void loginFailIfPasswordIsNotCorrect() {
        System.out.println("Test 4: " + this);
        userService.add(IVAN);
        Optional<User> userServiceLogin = userService.login(IVAN.getName(), "qwerty");
        Assertions.assertTrue(userServiceLogin.isEmpty());
    }

    @Test
    void loginFailIfUserDoesNotExist() {
        System.out.println("Test 5: " + this);
        userService.add(IVAN);
        Optional<User> userServiceLogin = userService.login("sdsdsd", IVAN.getPassword());
        Assertions.assertTrue(userServiceLogin.isEmpty());
    }

    @AfterEach
    void deleteDaaFromDatabase() {
        System.out.println("After each: " + this);
    }

    @AfterAll
    void closeConnectionPool() {
        System.out.println("After All: ");
    }
}
