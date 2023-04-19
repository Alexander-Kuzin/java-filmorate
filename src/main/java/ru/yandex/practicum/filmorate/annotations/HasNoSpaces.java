package ru.yandex.practicum.filmorate.annotations;

import ru.yandex.practicum.filmorate.validators.LoginSpacesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LoginSpacesValidator.class)
public @interface HasNoSpaces {
    String message() default "Login should not contains spaces";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
