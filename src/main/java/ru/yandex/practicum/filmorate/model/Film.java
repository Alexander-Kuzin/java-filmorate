package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.annotations.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class Film {
    private Long id;
    @NotBlank
    private String name;
    @Length(max = 200) @NotNull
    private String description;
    @ReleaseDate @NotNull
    private LocalDate releaseDate;
    @PositiveOrZero @NotNull
    private Integer duration;
    @JsonIgnore
    private Set<Long> likedFilms;

    public void addLikedFilm(Long userId) {
        if (this.likedFilms == null) {
            this.likedFilms = new HashSet<>();
        }
        this.likedFilms.add(userId);
    }
}
