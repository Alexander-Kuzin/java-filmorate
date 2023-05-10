package ru.yandex.practicum.filmorate.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.*;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

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
                           @PathVariable("userId") long userId) throws ValidationException {
        log.info("New request to delete like.");
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("New request to get all films");
        return new ArrayList<>(filmService.getAllFilms());
    }

    @GetMapping("/popular")
    public Collection<Film> getMostPopularFilms(@RequestParam(required = false) Optional<Integer> count) {
        log.info("New request to get most popular films.");
        if (count.isPresent()) {
            return filmService.getMostLikedFilms(count.get());
        }
        return filmService.getMostLikedFilms(10);
    }

    @DeleteMapping("{id}/delete")
    public void deleteFilm(@PathVariable("id") long id) {
        log.info("New request to get to delete film ID = {}.", id);
        filmService.deleteFilm(id);
    }
}
