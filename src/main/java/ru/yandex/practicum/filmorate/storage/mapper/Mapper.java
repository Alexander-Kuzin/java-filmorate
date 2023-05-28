package ru.yandex.practicum.filmorate.storage.mapper;

import lombok.SneakyThrows;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;

public class Mapper {

    @SneakyThrows
    public static User mapRowToUser(ResultSet resultSet, int rowNum) {
        return new User(
                resultSet.getLong("id"),
                resultSet.getString("login"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getDate("birthday").toLocalDate()
        );
    }

    @SneakyThrows
    public static Film mapRowToFilm(ResultSet resultSet, int rowNum) {
        return new Film(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getDate("releaseDate").toLocalDate(),
                resultSet.getInt("duration"),
                Mpa.valueOf(resultSet.getString("mpa"))
        );
    }

    @SneakyThrows
    public static Genre mapRowToGenre(ResultSet resultSet, int rowNum) {
        return Genre.valueOf(resultSet.getString("GENRE_NAME"));
    }

    @SneakyThrows
    public static Mpa mapRowToMpa(ResultSet resultSet, int rowNum) {
        return Mpa.valueOf(resultSet.getString("RATING_NAME"));
    }
}