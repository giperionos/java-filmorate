package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@AllArgsConstructor
public class FilmDirector {
    private Long filmId;
    private Integer directorId;
}
