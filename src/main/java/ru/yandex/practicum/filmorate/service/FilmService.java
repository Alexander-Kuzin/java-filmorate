package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    List<Film> getAllFilms();

    Film getFilmById(Long id);

    Film addNewFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getMostLikedFilms(Long count);

    Film addLike(Long idFilm, Long idUser);

    Film deleteLike(Long idFilm, Long idUser);
}