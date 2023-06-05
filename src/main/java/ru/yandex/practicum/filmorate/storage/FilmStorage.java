package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Optional<Film> addNewFilm(Film film);

    Optional<Film> getFilm(long id);

    Optional<Film> updateFilm(Film film);

    Optional<List<Film>> getAllFilms();

}
