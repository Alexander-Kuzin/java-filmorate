package ru.yandex.practicum.filmorate.annotations;

import ru.yandex.practicum.filmorate.validators.ReleaseDateFilmValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReleaseDateFilmValidator.class)
public @interface ReleaseDate {
    String message() default "Release date should be after 28 DECEMBER 1895";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
