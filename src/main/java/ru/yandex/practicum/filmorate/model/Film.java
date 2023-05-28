package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.annotations.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.*;

@Data
public class Film {

    private Long id;
    @NotBlank
    private String name;
    @NotNull
    @Length(max = 200)
    private String description;
    @NotNull
    @ReleaseDate
    private LocalDate releaseDate;
    @NotNull
    @PositiveOrZero
    private Integer duration;
    @JsonIgnore
    private Set<Long> likedFilms;

    private Mpa mpa;
    private Set<Genre> genres;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("FILM_ID", id);
        values.put("FILM_NAME", name);
        values.put("FILM_DESCRIPTION", description);
        values.put("RATING_ID", mpa.getId());
        values.put("RELEASE_DATE", releaseDate);
        values.put("DURATION", duration);
        return values;
    }

    public void addLikedFilm(Long userId) {
        if (this.likedFilms == null) {
            this.likedFilms = new HashSet<>();
        }
        this.likedFilms.add(userId);
    }

    public Film(long id, String name, String description, LocalDate releaseDate, int duration, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.likedFilms = new LinkedHashSet<>();
        this.genres = new LinkedHashSet<>();
    }
}
