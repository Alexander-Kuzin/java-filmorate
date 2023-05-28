package ru.yandex.practicum.filmorate.exceptions;

@Deprecated (since = "Sprint 10")
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(final String message) {
        super(message);
    }
}