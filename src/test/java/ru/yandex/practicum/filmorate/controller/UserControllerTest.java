package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {

    private static UserController userController;
    private static User user;

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @BeforeAll
    public static void beforeEach() {
        userController = new UserController();
        user = User.builder().id(1L).email("mail@ya.ru").birthday(LocalDate.now().minusDays(1)).login("login").username("username").build();
    }

    @Test
    void getAllUsersTest() {
        userController.addNewUSer(user);
        int size = userController.getAllUsers().size();
        assertEquals(1, size, "User не добавился =(");
    }

    @Test
    void addNewUserTest() {
        userController.addNewUSer(user);
        int size = userController.getAllUsers().size();
        assertEquals(1, size, "User не добавился =(");
        User testUser = user;
        testUser.setLogin("");
        assertEquals(1, validator.validate(testUser).size());
        testUser.setLogin(user.getLogin());
        testUser.setEmail("email");
        assertEquals(2, validator.validate(testUser).size());
        testUser.setEmail("testUser@yandex.ru");
        testUser.setBirthday(LocalDate.now().plusDays(1));

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validator.validate(testUser));

        assertEquals(String.format("BirthDate cannot be in future - %s", LocalDate.now().plusDays(1)),
                exception.getMessage());


        testUser.setBirthday(LocalDate.now().minusDays(7600));
        testUser.setId(2L);
        userController.addNewUSer(testUser);
        assertEquals(1, userController.getAllUsers().size());
    }

    @Test
    void addOrUpdateTest() {
        userController.addNewUSer(user);
        int size = userController.getAllUsers().size();
        assertEquals(1, size, "User не добавился =(");
        User testUser = user;
        testUser.setName("Test User");
        testUser.setLogin("Test user login");
        testUser.setEmail("testUser@yandex.ru");
        userController.addOrUpdateUser(testUser);
        User updatedUser = userController.getAllUsers().get(0);
        assertEquals(updatedUser, testUser);
    }
}