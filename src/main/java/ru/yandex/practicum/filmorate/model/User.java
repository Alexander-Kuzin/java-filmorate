package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotations.HasNoSpaces;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    private String name;
    @NotNull @PastOrPresent
    private LocalDate birthday;
    @JsonIgnore
    private Set<Long> friendsId;


    public void setFriendsId(Long friendsId) {
        if (this.friendsId == null) {
            this.friendsId = new HashSet<>();
        }
        this.friendsId.add(friendsId);
    }
}
