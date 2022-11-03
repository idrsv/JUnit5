package com.idrsv.junit.service;

import com.idrsv.junit.dto.User;
import com.idrsv.junit.paramresolver.UserServiceParamResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import javax.swing.text.html.Option;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Tag("fast")
@Tag("user")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith({
        UserServiceParamResolver.class
}) //указываем resolver
public class UserServiceTest {
    private static final User IVAN = User.of(1, "Ivan", "123");
    private static final User DANIL = User.of(2, "Danil", "1234");
    private UserService userService;

    UserServiceTest(TestInfo testInfo) {
        System.out.println();
    }

    @BeforeAll
    void init() {
        System.out.println("Before All: ");
    }

    @BeforeEach
    void prepare(UserService userService) {
        System.out.println("Before each: " + this);
        this.userService = userService;
    }

    @Test
    @Order(1)
    @DisplayName("Test 1 - Users will be empty if no user added")
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
    void usersConvertedToMapById() {
        System.out.println("Test 6: " + this);
        userService.add(IVAN, DANIL);
        Map<Integer, User> users = userService.getAllConvertedById();

        assertAll(
                () -> assertThat(users).containsKeys(IVAN.getID(), DANIL.getID()),
                () -> assertThat(users).containsValues(IVAN, DANIL)
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

    @Tag("Login")
    @Nested
    @DisplayName("Login Test's")
    class LoginTest {
        @Test
        void throwExceptionIfUserNameOrPasswordIsNull() {
            System.out.println("Test 7: " + this);
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> userService.login(null, "dummy")),
                    () -> assertThrows(IllegalArgumentException.class, () -> userService.login("dummy", null))
            );
        }


        @Test
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


        //Одним тестом закроем несколько кейсов
        @ParameterizedTest(name = "{displayName} LoginTest")
        //Предоставляет поток аргументов в метод
//        @ArgumentsSource()
//        @NullSource
//        @EmptySource
//        @NullAndEmptySource
//        @ValueSource(strings = {
//                "Ivan", "Danil"
//        })

        //Чаще всего
        @MethodSource("com.idrsv.junit.service.UserServiceTest#getArgumentsForLoginTest")
        @DisplayName("Login param test")
//        @CsvFileSource(resources = "/login-test-data.csv", delimiter = ',',numLinesToSkip = 1)
        void loginParametrizedTest(String name, String password, Optional<User> user) {
            userService.add(IVAN, DANIL);
            var mayBeUser = userService.login(name, password);
            assertThat(mayBeUser).isEqualTo(user);
        }


    }

    static Stream<Arguments> getArgumentsForLoginTest() {
        return Stream.of(
                Arguments.of("Ivan", "123", Optional.of(IVAN)),
                Arguments.of("Danil", "1234", Optional.of(DANIL)),
                Arguments.of("Danil", "dummy", Optional.empty()),
                Arguments.of("dummy", "1234", Optional.empty()));
    }
}
