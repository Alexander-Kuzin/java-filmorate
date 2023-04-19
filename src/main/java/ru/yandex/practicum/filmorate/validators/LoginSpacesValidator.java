package ru.yandex.practicum.filmorate.validators;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.annotations.HasNoSpaces;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class LoginSpacesValidator implements ConstraintValidator<HasNoSpaces, String> {

    @Override
    @SneakyThrows
    public boolean isValid(String login, ConstraintValidatorContext context) {
        log.debug("User login - " + login);
        boolean result = !login.contains(" ");
        if (!result) {
            throw new ValidationException(String.format("Login contains spaces - %s", login));
        }
        return result;
    }
}
