package ru.yandex.practicum.filmorate.validators;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.annotations.ReleaseDate;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Month;

@Slf4j
public class ReleaseDateFilmValidator implements ConstraintValidator<ReleaseDate, LocalDate> {

    @Override
    @SneakyThrows
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        log.debug("Validation of Release date - " + date);
        boolean result = date.isAfter(LocalDate.of(1895, Month.DECEMBER, 28));
        if (!result) {
            throw new ValidationException(String.format("Date validation error: Invalid date - %s", date));
        }
        return result;
    }
}
