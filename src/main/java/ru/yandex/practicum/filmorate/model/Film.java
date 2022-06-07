package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.validators.DateNotEarlier;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class Film {

    private Long id;

    @NotBlank(message = "Название фильма не должно быть пустым.")
    private String name;

    @Size(max = 200, message = "Описание фильма должно быть менее 200 символов.")
    private String description;

    @DateNotEarlier(value = "28.12.1895", message = "Дата релиза должна быть не ранее чем 28.12.1895.")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной.")
    private Integer duration;
}
