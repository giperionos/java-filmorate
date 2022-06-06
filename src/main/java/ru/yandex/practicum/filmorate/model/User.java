package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {

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
}
