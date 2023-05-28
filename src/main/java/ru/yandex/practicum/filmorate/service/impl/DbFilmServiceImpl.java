package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DbFilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms().orElse(new ArrayList<>());
    }

    @Override
    public Film getFilmById(Long id) {
        return filmStorage.getFilm(id).orElseThrow(() -> new EntityNotFoundException(String.format(
                "Entity %s ID = %s not found", Film.class.getName(), id)));
    }

    @Override
    public Film addNewFilm(Film film) {
        return filmStorage.addNewFilm(film).orElseThrow(() -> new EntityAlreadyExistException(String.format(
                "Entity %s ID = %s already exist", Film.class.getName(), film.getId())));
    }

    @Override
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film).orElseThrow(() -> new EntityNotFoundException(String.format(
                "Entity %s ID = %s not found", Film.class.getName(), film.getId())));
    }

    @Override
    public List<Film> getMostLikedFilms(Long count) {
        if (count == null) {
            count = 10L;
        }
        return filmStorage.getAllFilms().orElse(new ArrayList<>())
                .stream()
                .sorted(Collections.reverseOrder(Comparator.comparingInt(film -> film.getLikedFilms().size())))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Film addLike(Long idFilm, Long idUser) {
        User user = userStorage.getUser(idUser).orElseThrow(() -> new EntityNotFoundException(String.format(
                "Entity %s ID = %s not found", User.class.getName(), idUser)));
        Film film = filmStorage.getFilm(idFilm).orElseThrow(() -> new EntityNotFoundException(String.format(
                "Entity %s ID = %s not found", Film.class.getName(), idFilm)));
        film.getLikedFilms().add(user.getId());
        filmStorage.updateFilm(film);

        return film;
    }

    @Override
    public Film deleteLike(Long idFilm, Long idUser) {
        User user = userStorage.getUser(idUser).orElseThrow(() -> new EntityNotFoundException(String.format(
                "Entity %s ID = %s not found", User.class.getName(), idUser)));
        Film film = filmStorage.getFilm(idFilm).orElseThrow(() -> new EntityNotFoundException(String.format(
                "Entity %s ID = %s not found", Film.class.getName(), idFilm)));
        film.getLikedFilms().remove(user.getId());
        filmStorage.updateFilm(film);
        return film;
    }
}