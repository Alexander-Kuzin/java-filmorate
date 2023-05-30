package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

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
class FilmTests {
    private final FilmStorage filmStorage;
    private static Film FirstFilm;
    private static Film SecondFilm;

    @BeforeEach
    void setUpTestData() {
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
}