package ru.yandex.practicum.filmorate.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    private static FilmController filmController;
    private static Film film;
    private static Film badReleaseDateFilm;
    private static Film noNameFilm;
    private static Film bigDescriptionFilm;
    private static Film minusDur;
    private static Film filmToUpdate;
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController();
        film = new Film(1L, "Star Wars Episode 2", "Star Wars Episode 2",
                LocalDate.of(2022, 12, 20), 134);
        badReleaseDateFilm = new Film(2L, "Star Wars Episode 4", "Star Wars Episode 4",
                LocalDate.of(1895, 12, 24), 12);
        noNameFilm = new Film(3L, "", "Star Wars Episode 10",
                LocalDate.of(2022, 12, 20), 12);
        bigDescriptionFilm = new Film(4L, "Star Wars Episode 1", "Суматоха охватила Галактическую " +
                "республику. Налогообложение торговых маршрутов к отдаленным звездным системам спорное.\n" +
                "Надеясь решить вопрос с блокадой смертельных линкоров, жадная Торговая Федерация остановила всю " +
                "отгрузку на небольшую планету Naboo.\n" +
                "В то время как конгресс республики бесконечно обсуждает эту тревожную цепь событий, Главный канцлер " +
                "тайно послал двух Рыцарей джедаев, опекунов мира и справедливости в галактике, чтобы уладить " +
                "конфликт..", LocalDate.of(2022, 12, 20), 1
        );
        minusDur = new Film(5L, "Film", "Interesting Film",
                LocalDate.of(2022, 12, 20), -120);
        filmToUpdate = new Film(1L, "Star Wars Episode 2 : UPDATED", "Updated description",
                LocalDate.of(2020, 10, 10), 30);
    }


    @Test
    @SneakyThrows
    public void addFilmTest() {
        filmController.addNewFilm(film);
        int size = filmController.getAllFilms().size();
        assertEquals(1, size, "Фильм не записался =(");
        assertEquals(1, validator.validate(noNameFilm).size());
        assertEquals(1, validator.validate(bigDescriptionFilm).size());
        assertEquals(1, validator.validate(minusDur).size());
        assertEquals(1, validator.validate(noNameFilm).size());
    }

    @Test
    void updateFilmTest() {
        filmController.addNewFilm(film);
        int size = filmController.getAllFilms().size();
        assertEquals(1, size, "Фильм не записался =(");

        assertEquals(1, validator.validate(noNameFilm).size());
        assertEquals(1, validator.validate(bigDescriptionFilm).size());
        assertEquals(1, validator.validate(minusDur).size());
        assertEquals(1, validator.validate(noNameFilm).size());

        final ValidationException exception = assertThrows(
                ValidationException.class,

                () -> filmController.addOrUpdateFilm(badReleaseDateFilm));

        assertEquals("There is no film with ID 2",
                exception.getMessage());

        filmController.addOrUpdateFilm(filmToUpdate);
        Film example = filmController.getAllFilms().get(0);
        assertEquals(filmToUpdate, example, "Разные фильмы");
    }

    @Test
    void getAllFilmsTest() {
        int size = filmController.getAllFilms().size();
        assertEquals(0, size, "Not null size");

        filmController.addNewFilm(film);
        int size1 = filmController.getAllFilms().size();
        assertEquals(1, size1, "Фильм не записался =(");
    }
}