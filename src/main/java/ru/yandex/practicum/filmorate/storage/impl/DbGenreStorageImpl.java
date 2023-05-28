package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mapper.Mapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DbGenreStorageImpl implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<List<Genre>> getGenres() {
        String sqlQuery = "SELECT GENRE_NAME FROM GENRES";
        return Optional.of(jdbcTemplate.query(sqlQuery, Mapper::mapRowToGenre));
    }

    @Override
    public Optional<Genre> getGenre(Integer id) {
        String sqlQuery = "SELECT GENRE_NAME FROM GENRES WHERE GENRE_ID = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, Mapper::mapRowToGenre, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
}