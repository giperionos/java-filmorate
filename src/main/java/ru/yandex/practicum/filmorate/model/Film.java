package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.validators.DateNotEarlier;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@ToString
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

    @NotNull
    private MpaRating mpa;

    private Set<Genre> genres;

    private Set<Director> directors;

    public Film(Long id, String name, String description, LocalDate releaseDate, Integer duration, MpaRating mpa, Set<Genre> genres, Set<Director> directors) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genres;
        this.directors = directors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id.longValue() == film.getId().longValue()
                && name.equals(film.name)
                && description.equals(film.description)
                && releaseDate.equals(film.releaseDate)
                && duration.longValue() == film.duration.longValue()
                && mpa.getId().intValue() == film.mpa.getId().intValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id.longValue(), name, description, releaseDate, duration, mpa.getId().intValue());
    }
}
