package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Repository
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 0L;

    public Long getNewId() {
        return ++id;
    }

    public User addNewUser(User user) {
        users.put(user.getId(), user);
        log.info("Saved new User {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException(String.format("There is no user with ID %d", user.getId()));
        }
        users.put(user.getId(), user);
        log.info("User {} was updated", user);
        return user;
    }

    @Override
    public User getUser(long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(String.format("There is no user with ID %d", id));
        }
        return users.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public void deleteUser(long id) {
        log.info("Deleting user by ID = {}", id);
        users.remove(id);
    }

    public Collection<User> getUsersByIds(Collection<Long> ids) {
        List<User> result = new ArrayList<>();
        for (long userID : ids) {
            if (!users.containsKey(userID)) {
                throw new UserNotFoundException("There is no such ID");
            }
            result.add(users.get(userID));
        }
        return result;
    }
}