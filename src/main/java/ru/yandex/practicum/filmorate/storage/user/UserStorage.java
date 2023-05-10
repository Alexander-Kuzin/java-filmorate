package ru.yandex.practicum.filmorate.storage.user;


import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    User addNewUser(User user);

    User updateUser(User user);

    User getUser(long id);

    List<User> getAllUsers();

    void deleteUser(long id);

    Collection<User> getUsersByIds(Collection<Long> ids);

    Long getNewId();
}
