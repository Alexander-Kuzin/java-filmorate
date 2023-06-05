package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@Validated
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @SneakyThrows
    @PostMapping
    public Film addNewFilm(@Valid @RequestBody Film film) {
        log.info("New request to create film");
        log.debug("Film data {}", film);
        return filmService.addNewFilm(film);
    }

    @PutMapping
    @SneakyThrows
    public Film addOrUpdateFilm(@Valid @RequestBody Film film) {
        log.info("New request to add or update film");
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") long id) {
        log.info("New request get film by id {}", id);
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    @SneakyThrows
    public Film addLike(@PathVariable("id") long filmId,
                        @PathVariable("userId") long userId) {
        log.info("New request to add like.");
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") long filmId,
                           @PathVariable("userId") long userId) {
        log.info("New request to delete like.");
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("New request to get all films");
        return new ArrayList<>(filmService.getAllFilms());
    }

    @GetMapping("/popular")
    public Collection<Film> getMostPopularFilms(@Positive @RequestParam(defaultValue = "10") Long count) {
        log.info("New request to get most popular films.");
        return filmService.getMostLikedFilms(count);
    }

}
