package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@AllArgsConstructor
public class FilmGenre {

    private Long filmId;
    private Integer genreId;
}
