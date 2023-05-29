package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exceptions.EntityAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DbUserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers().orElse(new ArrayList<>());
    }

    @Override
    public User getUserById(Long id) {
        return userStorage.getUser(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(
                        "Entity %s ID = %s not found", User.class.getName(), id)));
    }

    @Override
    public User addNewUser(User user) {
        return userStorage.addNewUser(checkUserName(user)).orElseThrow(() ->
                new EntityAlreadyExistException(String.format(
                        "Entity %s ID = %s already exist", User.class.getName(), user.getId())));
    }

    @Override
    public User updateUser(User user) {
        return userStorage.updateUser(checkUserName(user)).orElseThrow(() ->
                new EntityNotFoundException(String.format(
                        "Entity %s ID = %s not found", User.class.getName(), user.getId())));
    }

    @Override
    public List<User> getUserFriends(Long id) {
        userStorage.getUser(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(
                        "Entity %s ID = %s not found", User.class.getName(), id)));

        return userStorage.getFriends(id).orElse(new ArrayList<>());
    }

    @Override
    public List<User> addFriend(Long idUser, Long idFriend) {
        userStorage.getUser(idUser).orElseThrow(() ->
                new EntityNotFoundException(String.format(
                        "Entity %s ID = %s not found", User.class.getName(), idUser)));
        userStorage.getUser(idFriend).orElseThrow(() ->
                new EntityNotFoundException(String.format(
                        "Entity %s ID = %s not found", User.class.getName(), idFriend)));

        return userStorage.addFriend(idUser, idFriend).orElse(new ArrayList<>());
    }

    @Override
    public List<User> deleteFriend(Long idUser, Long idFriend) {
        userStorage.getUser(idUser).orElseThrow(() ->
                new EntityNotFoundException(String.format(
                        "Entity %s ID = %s not found", User.class.getName(), idUser)));
        userStorage.getUser(idFriend).orElseThrow(() ->
                new EntityNotFoundException(String.format(
                        "Entity %s ID = %s not found", User.class.getName(), idFriend)));
        return userStorage.removeFriend(idUser, idFriend).orElse(new ArrayList<>());
    }

    @Override
    public List<User> getCommonFriends(Long idUser, Long idFriend) {
        userStorage.getUser(idUser).orElseThrow(() ->
                new EntityNotFoundException(String.format(
                        "Entity %s ID = %s not found", User.class.getName(), idUser)));
        userStorage.getUser(idFriend).orElseThrow(() ->
                new EntityNotFoundException(String.format(
                        "Entity %s ID = %s not found", User.class.getName(), idFriend)));
        List<User> userFriends = userStorage.getFriends(idUser).orElse(new ArrayList<>());
        List<User> friendFriends = userStorage.getFriends(idFriend).orElse(new ArrayList<>());
        if (userFriends.isEmpty() || friendFriends.isEmpty()) {
            return new ArrayList<>();
        }
        userFriends.retainAll(friendFriends);

        return userFriends.stream()
                .sorted(Comparator.comparingLong(User::getId))
                .collect(Collectors.toList());
    }

    private User checkUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return user;
    }
}