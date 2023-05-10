package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static Validator validator = validatorFactory.usingContext().getValidator();
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film addNewFilm(Film film) {
        Film filmToSave = Film.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .duration(film.getDuration())
                .releaseDate(film.getReleaseDate())
                .likedFilms(film.getLikedFilms())
                .build();
        validateAndSaveFilm(filmToSave);
        return filmToSave;
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
        return filmStorage
                .getAllFilms()
                .stream()
                .sorted(Comparator.comparingInt(f -> -f.getLikedFilms().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public void deleteFilm(long id) {
        filmStorage.deleteFilm(id);
    }

    private void validateAndSaveFilm(Film film) {
        try {
            Set<ConstraintViolation<Film>> validates = validator.validate(film);
            if (!validates.isEmpty()) {
                throw new ValidationException("Film data has incorrect values ");
            } else {
                saveFilm(film);
            }
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