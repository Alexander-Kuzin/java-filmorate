package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.mapper.Mapper;

import java.sql.PreparedStatement;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class DbFilmStorageImpl implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<List<Film>> getAllFilms() {
        String sqlQueryGetFilms = "SELECT F.FILM_ID id,\n" +
                "       FILM_NAME name,\n" +
                "       FILM_DESCRIPTION description,\n" +
                "       R.RATING_NAME mpa,\n" +
                "       RELEASE_DATE releaseDate,\n" +
                "       DURATION duration\n" +
                "FROM FILMS F\n" +
                "         LEFT JOIN RATINGS R on F.RATING_ID = R.RATING_ID\n" +
                "ORDER BY id";

        List<Film> films = jdbcTemplate.query(sqlQueryGetFilms, Mapper::mapRowToFilm);

        Map<Long, Film> mapFilms = films.stream()
                .collect(Collectors.toMap(Film::getId, Function.identity()));

        String sqlQueryGetGenres = "SELECT FILM_ID id,\n" +
                "       GENRE_NAME genreName\n" +
                "FROM GENRES_FILMS GF\n" +
                "    LEFT JOIN GENRES G on GF.GENRE_ID = G.GENRE_ID\n" +
                "ORDER BY id";

        List<Map<String, Object>> genresFilms = jdbcTemplate.queryForList(sqlQueryGetGenres);

        genresFilms.forEach(
                t -> mapFilms.get(Long.parseLong(t.get("id").toString())).getGenres().add(
                        Genre.valueOf(t.get("genreName").toString())
                ));

        String sqlQueryGetLikes = "SELECT USER_ID userId,\n" +
                "       FILM_ID  filmId\n" +
                "FROM LIKES_FILMS\n" +
                "ORDER BY filmId, userId";

        List<Map<String, Object>> likesFilms = jdbcTemplate.queryForList(sqlQueryGetLikes);

        likesFilms.forEach(
                t -> mapFilms.get(Long.parseLong(t.get("filmId").toString())).getLikedFilms().add(
                        Long.parseLong(t.get("userId").toString())
                ));

        return Optional.of(films);
    }


    @Override
    public Optional<Film> getFilm(long id) {
        String sqlQueryGetFilm = "SELECT F.FILM_ID id,\n" +
                "       F.FILM_NAME name,\n" +
                "       F.FILM_DESCRIPTION description,\n" +
                "       R.RATING_NAME mpa,\n" +
                "       F.RELEASE_DATE releaseDate,\n" +
                "       F.DURATION duration\n" +
                "FROM FILMS F\n" +
                "         LEFT JOIN RATINGS R on R.RATING_ID = F.RATING_ID\n" +
                "WHERE FILM_ID = ?\n";
        Film film;
        try {
            film = jdbcTemplate.queryForObject(sqlQueryGetFilm, Mapper::mapRowToFilm, id);
        } catch (DataAccessException e) {
            return Optional.empty();
        }

        assert film != null;

        String sqlQueryGetGenres = "SELECT GENRE_NAME genre\n" +
                "FROM GENRES_FILMS\n" +
                "LEFT JOIN GENRES G on G.GENRE_ID = GENRES_FILMS.GENRE_ID\n" +
                "WHERE FILM_ID = ?\n" +
                "ORDER BY G.GENRE_ID";

        List<Genre> genresFilms = jdbcTemplate.query(sqlQueryGetGenres, Mapper::mapRowToGenre, id);

        film.setGenres(new LinkedHashSet<>(genresFilms));

        String sqlQueryGetLikes = "SELECT USER_ID userId\n" +
                "FROM LIKES_FILMS\n" +
                "WHERE FILM_ID = ?\n" +
                "ORDER BY userId";

        List<Long> likesFilm = jdbcTemplate.query(sqlQueryGetLikes,
                (rs, rowNum) -> rs.getLong(1), id);

        film.setLikedFilms(new LinkedHashSet<>(likesFilm));

        return Optional.of(film);
    }

    @Override
    public Optional<Film> addNewFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("FILM_ID");

        long filmId = simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue();

        String sqlQueryAddGenres = "INSERT INTO GENRES_FILMS (FILM_ID, GENRE_ID)\n" +
                "values (?, ?)";

        List<Genre> genres = new ArrayList<>(film.getGenres());
        jdbcTemplate.batchUpdate(sqlQueryAddGenres, new BatchPreparedStatementSetter() {

            @Override
            @SneakyThrows
            public void setValues(PreparedStatement ps, int i) {
                ps.setLong(1, filmId);
                ps.setInt(2, genres.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });

        String sqlQueryAddLikes = "INSERT INTO LIKES_FILMS (FILM_ID, USER_ID)\n" +
                "values (?, ?)";

        List<Long> likes = new ArrayList<>(film.getLikedFilms());

        jdbcTemplate.batchUpdate(sqlQueryAddLikes, new BatchPreparedStatementSetter() {
            @Override
            @SneakyThrows
            public void setValues(PreparedStatement ps, int i) {
                ps.setLong(1, filmId);
                ps.setLong(2, likes.get(i));
            }

            @Override
            public int getBatchSize() {
                return likes.size();
            }
        });

        return getFilm(filmId);
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        String sqlQueryUpdateFilms = "UPDATE FILMS\n" +
                "set FILM_NAME = ?,\n" +
                "    FILM_DESCRIPTION = ?,\n" +
                "    RATING_ID = ?,\n" +
                "    RELEASE_DATE = ?,\n" +
                "    DURATION = ?\n" +
                "WHERE FILM_ID = ?";

        try {
            jdbcTemplate.update(sqlQueryUpdateFilms,
                    film.getName(),
                    film.getDescription(),
                    film.getMpa().getId(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getId()
            );
        } catch (DataAccessException e) {
            return Optional.empty();
        }

        String sqlQueryDeleteGenres = "DELETE FROM GENRES_FILMS\n" +
                "WHERE FILM_ID = ?";

        jdbcTemplate.update(sqlQueryDeleteGenres, film.getId());

        String sqlQueryAddGenres = "INSERT INTO GENRES_FILMS (FILM_ID, GENRE_ID)\n" +
                "values (?, ?)";

        List<Genre> genres = new ArrayList<>(film.getGenres());
        jdbcTemplate.batchUpdate(sqlQueryAddGenres, new BatchPreparedStatementSetter() {
            @Override
            @SneakyThrows
            public void setValues(PreparedStatement ps, int i) {
                ps.setLong(1, film.getId());
                ps.setInt(2, genres.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });

        String sqlQueryDeleteLikes = "DELETE FROM LIKES_FILMS\n" +
                "WHERE FILM_ID = ?";

        jdbcTemplate.update(sqlQueryDeleteLikes, film.getId());
        String sqlQueryAddLikes = "INSERT INTO LIKES_FILMS (FILM_ID, USER_ID)\n" +
                "values (?, ?)";
        List<Long> likes = new ArrayList<>(film.getLikedFilms());
        jdbcTemplate.batchUpdate(sqlQueryAddLikes, new BatchPreparedStatementSetter() {
            @Override
            @SneakyThrows
            public void setValues(PreparedStatement ps, int i) {
                ps.setLong(1, film.getId());
                ps.setLong(2, likes.get(i));
            }

            @Override
            public int getBatchSize() {
                return likes.size();
            }
        });

        return getFilm(film.getId());
    }

}