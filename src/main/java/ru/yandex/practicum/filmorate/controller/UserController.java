package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @SneakyThrows
    public User addNewUser(@Valid @RequestBody User user) {
        log.info("New request to create user");
        log.debug("User data {}", user);
        return userService.addNewUser(user);
    }

    @PutMapping
    @SneakyThrows
    public User addOrUpdateUser(@Valid @RequestBody User user) {
        log.info("New request to add or update user");
        return userService.updateUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("New request to get all users");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @SneakyThrows
    public User getUser(@PathVariable("id") long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    @SneakyThrows
    public Collection<User> getUserFriends(@PathVariable("id") long id) {
        return userService.getUserFriends(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @SneakyThrows
    public void addFriend(@PathVariable("id") long userId,
                          @PathVariable("friendId") long friendId) {
        log.info("New request to add friend.");
        userService.addFriend(userId, friendId);
        log.info("New Friend with id {} was added", friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @SneakyThrows
    public void deleteFriend(@PathVariable("id") long userId,
                             @PathVariable("friendId") long friendId) {
        log.info("New request to delete friend.");
        userService.deleteFriend(userId, friendId);
        log.info("Friend was deleted.");
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @SneakyThrows
    public Collection<User> getCommonFriends(@PathVariable("id") long userId,
                                             @PathVariable("otherId") long otherId) {
        log.info("New request to get common friends.");
        return userService.getCommonFriends(userId, otherId);
    }

}
