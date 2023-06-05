package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserTest {
    private final UserStorage userStorage;
    private static User userOne;
    private static User userTwo;


    @BeforeEach
    void setUpTestData() {
        userOne = new User(0, "FirstUserLogin", "FirstUser", "FirstUser@email.ru",
                LocalDate.of(1991, 4, 3));
        userTwo = new User(0, "SecondUserLogin", "SecondUser", "yandex@yandex.ru",
                LocalDate.of(1992, 5, 4));
    }


    @Test
    void emptyGeAllUsersTest() {
        Optional<List<User>> usersOptional = userStorage.getAllUsers();
        assertTrue(usersOptional.isPresent());
        assertTrue(usersOptional.get().isEmpty());
    }

    @Test
    void addNewUserTest() {
        Optional<User> userOptional = userStorage.addNewUser(userOne);
        assertThat(userOptional).isPresent().hasValueSatisfying(user -> {
            assertThat(user).hasFieldOrPropertyWithValue("id", 1L);
            assertThat(user).hasFieldOrPropertyWithValue("name", "FirstUser");
            assertThat(user).hasFieldOrPropertyWithValue("email", "FirstUser@email.ru");
            assertThat(user).hasFieldOrPropertyWithValue("login", "FirstUserLogin");
            assertThat(user).hasFieldOrPropertyWithValue("birthday", LocalDate.of(1991, 4, 3));
        });
    }

    @Test
    void getAllUsersTest() {
        userStorage.addNewUser(userOne);
        Optional<List<User>> usersOptional = userStorage.getAllUsers();
        assertTrue(usersOptional.isPresent());
        assertEquals(usersOptional.get().size(), 1);
        assertThat(usersOptional).isPresent().hasValueSatisfying(list -> {
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("id", 1L);
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("name", "FirstUser");
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("email", "FirstUser@email.ru");
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("login", "FirstUserLogin");
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("birthday", LocalDate.of(1991, 4, 3));
        });

        userStorage.addNewUser(userTwo);
        usersOptional = userStorage.getAllUsers();
        assertTrue(usersOptional.isPresent());
        assertEquals(usersOptional.get().size(), 2);
        assertThat(usersOptional).isPresent().hasValueSatisfying(list -> {
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("id", 1L);
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("name", "FirstUser");
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("email", "FirstUser@email.ru");
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("login", "FirstUserLogin");
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("birthday", LocalDate.of(1991, 4, 3));
            assertThat(list.get(1)).hasFieldOrPropertyWithValue("id", 2L);
            assertThat(list.get(1)).hasFieldOrPropertyWithValue("name", "SecondUser");
            assertThat(list.get(1)).hasFieldOrPropertyWithValue("email", "yandex@yandex.ru");
            assertThat(list.get(1)).hasFieldOrPropertyWithValue("login", "SecondUserLogin");
            assertThat(list.get(1)).hasFieldOrPropertyWithValue("birthday", LocalDate.of(1992, 5, 4));
        });
    }

    @Test
    void updateUserTest() {
        userStorage.addNewUser(userOne);
        userTwo.setId(1L);
        userStorage.updateUser(userTwo);
        Optional<List<User>> usersOptional = userStorage.getAllUsers();
        assertTrue(usersOptional.isPresent());
        assertEquals(usersOptional.get().size(), 1);
        assertThat(usersOptional).isPresent().hasValueSatisfying(list -> {
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("id", 1L);
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("name", "SecondUser");
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("email", "yandex@yandex.ru");
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("login", "SecondUserLogin");
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("birthday", LocalDate.of(1992, 5, 4));
        });
    }


    @Test
    void addFriendTest() {
        userStorage.addNewUser(userOne);
        userStorage.addNewUser(userTwo);
        Optional<List<User>> friendsUserOne = userStorage.addFriend(1L, 2L);
        assertTrue(friendsUserOne.isPresent());
        assertEquals(friendsUserOne.get().size(), 1);
        assertThat(friendsUserOne).isPresent().hasValueSatisfying(list -> {
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("id", 2L);
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("name", "SecondUser");
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("email", "yandex@yandex.ru");
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("login", "SecondUserLogin");
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("birthday", LocalDate.of(1992, 5, 4));
        });
        Optional<List<User>> friendsUserTwo = userStorage.getFriends(2L);
        assertTrue(friendsUserTwo.isPresent());
        assertTrue(friendsUserTwo.get().isEmpty());
    }

    @Test
    void removeFriendTest() {
        userStorage.addNewUser(userOne);
        userStorage.addNewUser(userTwo);
        userStorage.addFriend(1L, 2L);
        Optional<List<User>> friendsUserOne = userStorage.removeFriend(1L, 2L);
        assertTrue(friendsUserOne.isPresent());
        assertTrue(friendsUserOne.get().isEmpty());
    }
}