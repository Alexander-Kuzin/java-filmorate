package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GenreTests {

    private final GenreStorage genreStorage;

    @Test
    public void getAllGenresTest() {
        assertEquals(genreStorage.getGenres().orElse(new ArrayList<>()).size(), 6);
    }

    @Test
    public void getGenreByIdTest() {
        assertEquals(genreStorage.getGenre(1).orElse(null), Genre.COMEDY);
        assertEquals(genreStorage.getGenre(6).orElse(null), Genre.ACTION);
    }
}