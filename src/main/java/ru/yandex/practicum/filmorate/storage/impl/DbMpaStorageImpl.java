package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.mapper.Mapper;

import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class DbMpaStorageImpl implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<List<Mpa>> getAllMpa() {
        String sqlQuery = "SELECT RATING_NAME FROM RATINGS";
        return Optional.of(jdbcTemplate.query(sqlQuery, Mapper::mapRowToMpa));
    }

    @Override
    public Optional<Mpa> getMpa(Integer id) {
        String sqlQuery = "SELECT RATING_NAME FROM RATINGS WHERE RATING_ID = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, Mapper::mapRowToMpa, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
}