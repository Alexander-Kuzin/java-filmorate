package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DbGenreServiceImpl implements GenreService {
    private final GenreStorage genreStorage;

    @Override
    public List<Genre> getGenres() {
        return genreStorage.getGenres().orElse(new ArrayList<>());
    }

    @Override
    public Genre getGenre(Integer id) {
        return genreStorage.getGenre(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(
                        "Entity %s ID = %s not found", Genre.class.getName(), id)));
    }
}