package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validators.DateNotEarlier;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {

    private Long id;

    @NotNull(message = "Не указано название фильма.")
    @NotBlank(message = "Указано пустое название фильма.")
    private String name;

    @Size(max = 200, message = "К фильму дано слишком длинное описание.")
    private String description;

    @DateNotEarlier(value = "28.12.1895", message = "Указана некорректная дата релиза.")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной.")
    private Integer duration;
}
