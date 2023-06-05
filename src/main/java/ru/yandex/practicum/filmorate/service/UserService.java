package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long id);

    User addNewUser(User user);

    User updateUser(User user);

    List<User> getUserFriends(Long id);

    List<User> addFriend(Long idUser, Long idFriend);

    List<User> deleteFriend(Long idUser, Long idFriend);

    List<User> getCommonFriends(Long idUser, Long idFriend);
}