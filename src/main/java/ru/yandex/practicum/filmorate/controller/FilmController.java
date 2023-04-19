package ru.yandex.practicum.filmorate.controller;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.*;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static Validator validator = validatorFactory.usingContext().getValidator();

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    private final Map<Long, Film> films = new HashMap<>();
    Long id = 0L;

    private Long getNewFilmId() {
        return ++id;
    }

    @SneakyThrows
    @PostMapping
    public Film addNewFilm(@Valid @RequestBody Film film) {
        log.info("New request to create film");
        log.debug("Film data {}", film);
        Film filmToSave = Film.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .duration(film.getDuration())
                .releaseDate(film.getReleaseDate())
                .build();
        validateAndSaveFilm(filmToSave);
        return filmToSave;
    }

    @PutMapping
    @SneakyThrows
    public Film addOrUpdateFilm(@Valid @RequestBody Film film) {
        log.info("New request to add or update film");
        if (film.getId() == null) {
            throw new ValidationException("ID cannot be null");
        }

        if (films.containsKey(film.getId())) {
            validateAndSaveFilm(film);
            films.put(film.getId(), film);
        } else {
            log.error("Incorrect request value ID");
            throw new ValidationException(String.format("There is no film with ID %d", film.getId()));
        }
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("New request to get all films");
        return new ArrayList<>(films.values());
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
            film.setId(getNewFilmId());
        }

        if (films.containsKey(film.getId())) {
            log.warn(String.format("There is a film with id %d", film.getId()));
        } else {
            films.put(film.getId(), film);
            log.info("Saved film {}", film);
        }
    }
}
