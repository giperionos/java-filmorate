package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class FilmDirector {
    private Long filmId;
    private Integer directorId;

    public FilmDirector(Long filmId, Integer directorId) {
        this.filmId = filmId;
        this.directorId = directorId;
    }
}
