package ru.yandex.practicum.filmorate.exceptions;

@Deprecated (since = "Sprint 10")
public class FilmNotFoundException extends RuntimeException {
    public FilmNotFoundException(final String message) {
        super(message);
    }
}
