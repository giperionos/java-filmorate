package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {

    private Long id;

    @NotNull(message = "Не указан email пользователя.")
    @Email(message = "Не верно указан email пользователя.")
    private String email;

    @NotBlank(message = "Не указан login.")
    @Pattern(regexp = "^[^\\s]+$", message = "Указанный login содержит пробелы.")
    private String login;


    private String name;

    @Past(message = "Дата рождения должна быть в прошлом.")
    private LocalDate birthday;
}
