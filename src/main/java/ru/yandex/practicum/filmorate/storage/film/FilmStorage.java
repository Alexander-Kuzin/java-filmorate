package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film addNewFilm(Film film);

    Film getFilm(long id);

    Film updateFilm(Film film);

    void deleteFilm(long id);

    List<Film> getAllFilms();

    Long getNewId();
}
