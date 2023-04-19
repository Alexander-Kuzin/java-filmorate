package ru.yandex.practicum.filmorate.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.*;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static Validator validator = validatorFactory.usingContext().getValidator();
    private final Map<Long, User> users = new HashMap<>();
    private Long id = 0L;

    @PostMapping
    @SneakyThrows
    public User addNewUSer(@Valid @RequestBody User user) {
        log.info("New request to create user");
        log.debug("User data {}", user);

        User userToCreate = User.builder()
                .id(getNewUserId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();
        validateAndSaveUser(userToCreate);
        return userToCreate;
    }

    @PutMapping
    @SneakyThrows
    public User addOrUpdateUser(@Valid @RequestBody User user) {
        log.info("New request to add or update user");
        if (user.getId() == null) {
            throw new ValidationException("User ID cannot be null");
        }

        if (users.containsKey(user.getId())) {
            validateAndSaveUser(user);
            users.put(user.getId(), user);
        } else {
            log.error("Incorrect request value ID");
            throw new ValidationException(String.format("There is no user with ID %d", user.getId()));
        }
        return user;
    }

    private void validateAndSaveUser(User user) {
        try {
            if (StringUtils.isBlank(user.getName())) {
                user.setName(user.getLogin());
            }

            Set<ConstraintViolation<User>> validates = validator.validate(user);
            if (!validates.isEmpty()) {
                throw new ValidationException("User data has incorrect values ");
            } else {
                saveUser(user);
            }
        } catch (ValidationException e) {
            log.error(String.format("Check user data %s ", user));
        }
    }

    private void saveUser(User user) {
        if (user.getId() == null) {
            user.setId(getNewUserId());
        }

        if (users.containsKey(user.getId())) {
            log.warn(String.format("There is a user with id %d", user.getId()));
        } else {
            users.put(user.getId(), user);
            log.info("Saved user {}", user);
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("New request to get all users");
        return new ArrayList<>(users.values());
    }

    private Long getNewUserId() {
        return ++id;
    }
}
