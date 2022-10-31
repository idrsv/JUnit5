package com.idrsv.junit.service;

import com.idrsv.junit.dto.User;
import org.junit.jupiter.api.*;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Tag("fast")
@Tag("user")
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
        Assertions.assertTrue(users.isEmpty(), "User list should be empty");
    }

    @Test
    void usersSizeIfUserAdded() {
        System.out.println("Test 2: " + this);
        userService.add(IVAN);
        userService.add(DANIL);
        var users = userService.getAll();

        assertThat(users).hasSize(2);
//        Assertions.assertEquals(2, users.size());
    }

    @Test
    @Tag("Login")
    void loginSuccessIfUserExist() {
        System.out.println("Test 3: " + this);
        userService.add(IVAN);
        Optional<User> userServiceLogin = userService.login(IVAN.getName(), IVAN.getPassword());

        assertThat(userServiceLogin).isPresent();
        userServiceLogin.ifPresent(user -> assertThat(user).isEqualTo(IVAN));

//        Assertions.assertTrue(userServiceLogin.isPresent());
//        userServiceLogin.ifPresent(user -> Assertions.assertEquals(IVAN, user));
    }

    @Test
    @Tag("Login")
    void loginFailIfPasswordIsNotCorrect() {
        System.out.println("Test 4: " + this);
        userService.add(IVAN);
        Optional<User> userServiceLogin = userService.login(IVAN.getName(), "qwerty");
        Assertions.assertTrue(userServiceLogin.isEmpty());
    }

    @Test
    @Tag("Login")
    void loginFailIfUserDoesNotExist() {
        System.out.println("Test 5: " + this);
        userService.add(IVAN);
        Optional<User> userServiceLogin = userService.login("sdsdsd", IVAN.getPassword());
        Assertions.assertTrue(userServiceLogin.isEmpty());
    }

    @Test
    void usersConvertedToMapById() {
        System.out.println("Test 6: " + this);
        userService.add(IVAN,DANIL);
        Map<Integer, User> users =  userService.getAllConvertedById();

        assertAll(
                () -> assertThat(users).containsKeys(IVAN.getID(), DANIL.getID()),
                () -> assertThat(users).containsValues(IVAN, DANIL)
        );
    }

    @Test
    @Tag("Login")
    void throwExceptionIfUserNameOrPasswordIsNull() {
        System.out.println("Test 7: " + this);
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> userService.login(null, "dummy")),
                () -> assertThrows(IllegalArgumentException.class, () -> userService.login("dummy", null))
        );
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
