package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.annotations.HasNoSpaces;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class User {

    private Long id;
    @JsonIgnore
    private String username;
    @Email(message = "Please input correct email")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank
    @HasNoSpaces
    private String login;
    private String name;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;
    @JsonIgnore
    private Set<Long> friendsId;

    public void setFriendsId(Long friendsId) {
        if (this.friendsId == null) {
            this.friendsId = new HashSet<>();
        }
        this.friendsId.add(friendsId);
    }

    public User(long id, String login, String name, String email, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("USER_ID", id);
        values.put("EMAIL", email);
        values.put("LOGIN", login);
        values.put("USER_NAME", name);
        values.put("BIRTHDAY", birthday);
        return values;
    }
}
