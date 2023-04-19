package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotations.BirthDay;
import ru.yandex.practicum.filmorate.annotations.HasNoSpaces;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class User {
    private Long id;
    private String username;
    @Email(message = "Please input correct email")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank
    @HasNoSpaces
    private String login;
    private String name; // fixme имя для отображения может быть пустым — в таком случае будет использован логин;
    @BirthDay
    private LocalDate birthday;


}
