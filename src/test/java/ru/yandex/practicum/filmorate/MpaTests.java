package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MpaTests {

    private final MpaStorage mpaStorage;

    @Test
    void getAllMpaTest() {
        assertEquals(mpaStorage.getAllMpa().orElse(new ArrayList<>()).size(), 5);
    }

    @Test
    void getMpaByIdTest() {
        assertEquals(mpaStorage.getMpa(1).orElse(null), Mpa.G);
        assertEquals(mpaStorage.getMpa(2).orElse(null), Mpa.PG);
        assertEquals(mpaStorage.getMpa(3).orElse(null), Mpa.PG13);
        assertEquals(mpaStorage.getMpa(4).orElse(null), Mpa.R);
        assertEquals(mpaStorage.getMpa(5).orElse(null), Mpa.NC17);
    }
}