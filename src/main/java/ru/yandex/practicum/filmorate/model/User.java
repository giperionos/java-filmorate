package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class User {

    public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        defineName();
    }

    private Long id;

    @NotNull(message = "Не указан email пользователя.")
    @Email(message = "Указанный email пользователя не сооветствует формату email.")
    private String email;

    @NotBlank(message = "Не указан login пользователя.")
    @Pattern(regexp = "^[^\\s]+$", message = "Указанный login пользователя содержит пробелы.")
    private String login;

    private String name;

    @Past(message = "Дата рождения пользователя должна быть в прошлом.")
    private LocalDate birthday;

    private void defineName(){
        if (this.getName().isEmpty() || this.getName().isBlank()) {
            this.setName(this.getLogin());
        }
    }
}
