package ru.yandex.practicum.filmorate.validators;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.annotations.BirthDay;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;


@Slf4j
public class BirthDayValidator implements ConstraintValidator<BirthDay, LocalDate> {

    @Override
    @SneakyThrows
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        log.debug("User birth date - " + birthDate);
        boolean result = birthDate.isBefore(LocalDate.now());
        if (!result) {
            throw new ValidationException(String.format("BirthDate cannot be in future - %s", birthDate));
        }
        return result;
    }
}
