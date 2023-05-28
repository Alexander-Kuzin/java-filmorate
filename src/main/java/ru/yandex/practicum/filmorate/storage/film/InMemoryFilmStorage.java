package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Repository
@Slf4j
@Deprecated(since = "Sprint 11")
public class InMemoryFilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private Long id = 0L;

    public Long getNewId() {
        return ++id;
    }


    public Film addNewFilm(Film film) {
        films.put(film.getId(), film);
        log.info("Saved new Film - {} ", film);
        return film;
    }


    public Film getFilm(long id) {
        return Optional.ofNullable(films.get(id)).orElseThrow(() -> new FilmNotFoundException("There is no Film with id = " + id));
    }


    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotFoundException("There is no Film with id = " + id);
        }
        films.put(film.getId(), film);
        log.info("Film {} was updated", film);
        return film;
    }


    public void deleteFilm(long id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException("There is no Film with id = " + id);
        }
        films.remove(id);
        log.info("Film ID = {} was deleted", id);
    }


    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }
}
