package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Deprecated (since = "Sprint 11")
public class InMemoryFilmService {

    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;

    public Film addNewFilm(Film film) {
        validateAndSaveFilm(film);
        return film;
    }

    @SneakyThrows
    public Film updateFilm(Film film) {
        if (film.getId() == null) {
            throw new ValidationException("ID cannot be null");
        }

        if (filmStorage.getFilm(film.getId()) == null) {
            log.error("Incorrect request value ID");
            throw new ValidationException(String.format("There is no film with ID %d", film.getId()));
        }

        validateAndSaveFilm(film);
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(long filmId) {
        return filmStorage.getFilm(filmId);
    }

    public Film addLike(long filmId, long userId) {
        User user = userStorage.getUser(userId);
        Film film = filmStorage.getFilm(filmId);
        film.addLikedFilm(user.getId());
        return film;
    }

    public void deleteLike(long filmId, long userId) {
        User user = userStorage.getUser(userId);
        Film film = filmStorage.getFilm(filmId);
        film.getLikedFilms().remove(user.getId());
    }

    public Collection<Film> getMostLikedFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted(Collections.reverseOrder(Comparator.comparingInt(film -> film.getLikedFilms().size())))
                .limit(count)
                .collect(Collectors.toList());
    }

    public void deleteFilm(long id) {
        filmStorage.deleteFilm(id);
    }

    private void validateAndSaveFilm(Film film) {
        try {
            saveFilm(film);
        } catch (ValidationException e) {
            log.error(String.format("Check film data %s ", film.toString()));
        }
    }

    private void saveFilm(Film film) {
        if (film.getId() == null) {
            film.setId(filmStorage.getNewId());
        }
        if (film.getLikedFilms() == null) {
            film.setLikedFilms(new HashSet<>());
        }
        filmStorage.addNewFilm(film);
        log.info("Saved film {}", film);
    }
}