package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DbMpaServiceImpl implements MpaService {
    private final MpaStorage mpaStorage;

    @Override
    public List<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa().orElse(new ArrayList<>());
    }

    @Override
    public Mpa getMpaById(Integer id) {
        return mpaStorage.getMpa(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(
                        "Entity %s not found", Mpa.class.getName())));
    }
}
