package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest()
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmorateApplicationTests {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    private static User userOne;
    private static User userTwo;
    private static Film FirstFilm;
    private static Film SecondFilm;

    @BeforeEach
    void setUpTestData() {
        userOne = new User(0, "FirstUserLogin", "FirstUser", "FirstUser@email.ru",
                LocalDate.of(1991, 4, 3));
        userTwo = new User(0, "SecondUserLogin", "SecondUser", "yandex@yandex.ru",
                LocalDate.of(1992, 5, 4));
        FirstFilm = new Film(0, "FirstFilm", "FirstDescription",
                LocalDate.of(1945, 5, 9), 120, Mpa.G);
        SecondFilm = new Film(0, "SecondFilm", "SecondDescription",
                LocalDate.of(2023, 5, 21), 180, Mpa.NC17);
    }

    @Test
    void getAllFilmOptionalPresentTest() {
        Optional<List<Film>> films = filmStorage.getAllFilms();
        assertTrue(films.isPresent());
        assertTrue(films.get().isEmpty());
    }

    @Test
    void addNewFilmTest() {
        Optional<Film> filmOptional = filmStorage.addNewFilm(FirstFilm);
        assertThat(filmOptional).isPresent().hasValueSatisfying(film -> {
            assertThat(film).hasFieldOrPropertyWithValue("id", 1L);
            assertThat(film).hasFieldOrPropertyWithValue("name", "FirstFilm");
            assertThat(film).hasFieldOrPropertyWithValue("description", "FirstDescription");
            assertThat(film).hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1945, 5, 9));
            assertThat(film).hasFieldOrPropertyWithValue("duration", 120);
            assertThat(film).hasFieldOrPropertyWithValue("mpa", Mpa.G);
        });
    }

    @Test
    void getAllFilmsTest() {
        filmStorage.addNewFilm(FirstFilm);
        Optional<List<Film>> filmsOptional = filmStorage.getAllFilms();
        assertTrue(filmsOptional.isPresent());
        assertEquals(filmsOptional.get().size(), 1);
        assertThat(filmsOptional).isPresent().hasValueSatisfying(list -> {
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("id", 1L);
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("name", "FirstFilm");
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("description", "FirstDescription");
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1945, 5, 9));
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("duration", 120);
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("mpa", Mpa.G);
        });

        filmStorage.addNewFilm(SecondFilm);
        filmsOptional = filmStorage.getAllFilms();
        assertTrue(filmsOptional.isPresent());
        assertEquals(filmsOptional.get().size(), 2);
        assertThat(filmsOptional).isPresent().hasValueSatisfying(list -> {
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("id", 1L);
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("name", "FirstFilm");
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("description", "FirstDescription");
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1945, 5, 9));
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("duration", 120);
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("mpa", Mpa.G);
            assertThat(list.get(1)).hasFieldOrPropertyWithValue("id", 2L);
            assertThat(list.get(1)).hasFieldOrPropertyWithValue("name", "SecondFilm");
            assertThat(list.get(1)).hasFieldOrPropertyWithValue("description", "SecondDescription");
            assertThat(list.get(1)).hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2023, 5, 21));
            assertThat(list.get(1)).hasFieldOrPropertyWithValue("duration", 180);
            assertThat(list.get(1)).hasFieldOrPropertyWithValue("mpa", Mpa.NC17);
        });
    }

    @Test
    void updateFilmTest() {
        filmStorage.addNewFilm(FirstFilm);
        SecondFilm.setId(1L);
        filmStorage.updateFilm(SecondFilm);
        Optional<List<Film>> filmsOptional = filmStorage.getAllFilms();

        assertTrue(filmsOptional.isPresent());
        assertEquals(filmsOptional.get().size(), 1);
        assertThat(filmsOptional).isPresent().hasValueSatisfying(list -> {
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("id", 1L);
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("name", "SecondFilm");
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("description", "SecondDescription");
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2023, 5, 21));
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("duration", 180);
            assertThat(list.get(0)).hasFieldOrPropertyWithValue("mpa", Mpa.NC17);
        });
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
    public void getAllGenresTest() {
        assertEquals(genreStorage.getGenres().orElse(new ArrayList<>()).size(), 6);
    }

    @Test
    public void getGenreByIdTest() {
        assertEquals(genreStorage.getGenre(1).orElse(null), Genre.COMEDY);
        assertEquals(genreStorage.getGenre(6).orElse(null), Genre.ACTION);
    }

    @Test
    void getAllMpaTest() {
        assertEquals(mpaStorage.getAllMpa().orElse(new ArrayList<>()).size(), 5);
    }

    @Test
    void getMpaByIdTest() {
        assertEquals(mpaStorage.getMpa(1).orElse(null), Mpa.G);
        assertEquals(mpaStorage.getMpa(2).orElse(null), Mpa.PG);
        assertEquals(mpaStorage.getMpa(3).orElse(null), Mpa.PG13);
        assertEquals(mpaStorage.getMpa(4).orElse(null), Mpa.R);
        assertEquals(mpaStorage.getMpa(5).orElse(null), Mpa.NC17);
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