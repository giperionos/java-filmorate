package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Likes {
    private Long filmId;
    private Long userId;

    public Likes(Long filmId, Long userId) {
        this.filmId = filmId;
        this.userId = userId;
    }
}
