package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<List<User>> getAllUsers();

    Optional<User> getUser(Long id);

    Optional<User> addNewUser(User user);

    Optional<User> updateUser(User user);

    Optional<List<User>> getFriends(Long id);

    Optional<List<User>> addFriend(Long idUser, Long idFriend);

    Optional<List<User>> removeFriend(Long idUser, Long idFriend);
}