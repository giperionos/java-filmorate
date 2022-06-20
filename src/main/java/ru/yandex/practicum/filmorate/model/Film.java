package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.validators.DateNotEarlier;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Film extends Entity {

    public Film(Long id, String name, String description, LocalDate releaseDate, Integer duration) {
        super(id);
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = new HashSet<>();
    }

    @NotBlank(message = "Название фильма не должно быть пустым.")
    private String name;

    @Size(max = 200, message = "Описание фильма должно быть менее 200 символов.")
    private String description;

    @DateNotEarlier(value = "28.12.1895", message = "Дата релиза должна быть не ранее чем 28.12.1895.")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной.")
    private Integer duration;

    private Set<Long> likes;
}
