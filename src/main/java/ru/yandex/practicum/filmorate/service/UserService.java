package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    UserStorage userStorage;
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static Validator validator = validatorFactory.usingContext().getValidator();

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addNewUser(User user) {
        User userToCreate = User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .friendsId(user.getFriendsId())
                .build();
        validateAndSaveUser(userToCreate);
        return userToCreate;
    }

    public User updateUser(User user) throws ValidationException {
        if (user.getId() == null) {
            throw new ValidationException("User ID cannot be null");
        }
        if (userStorage.getUser(user.getId()) != null) {
            userStorage.updateUser(user);
        } else {
            log.error("Incorrect request value ID");
            throw new ValidationException(String.format("There is no user with ID %d", user.getId()));
        }
        return user;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUser(long id) {
        return userStorage.getUser(id);
    }

    public void addFriend(long userId, long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        user.setFriendsId(friend.getId());
        friend.setFriendsId(user.getId());
    }

    public void deleteFriend(long userId, long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        user.getFriendsId().remove(friend.getId());
        friend.getFriendsId().remove(user.getId());
    }

    public Collection<User> getUserFriends(long id) {
        User userFriends = userStorage.getUser(id);
        return userStorage.getUsersByIds(userFriends.getFriendsId());
    }

    public Collection<User> getCommonFriends(long userId, long otherId) {
        User user1 = userStorage.getUser(userId);
        User user2 = userStorage.getUser(otherId);
        Set<Long> commonFriendIds = findCommonElements(user1.getFriendsId(), user2.getFriendsId());
        return userStorage.getUsersByIds(commonFriendIds);
    }

    private static <T> Set<T> findCommonElements(Collection<T> first, Collection<T> second) {
        if (first != null || second != null) {
            return first.stream().filter(second::contains).collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    public void deleteUser(long id) {
        userStorage.deleteUser(id);
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
            user.setId(userStorage.getNewId());
        }
        userStorage.addNewUser(user);
        log.info("Saved user {}", user);
    }
}