package ru.yandex.practicum.filmorate.annotations;

import ru.yandex.practicum.filmorate.validators.BirthDayValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Deprecated
@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BirthDayValidator.class)
public @interface BirthDay {
    String message() default "BirthDay cannot be in future!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
