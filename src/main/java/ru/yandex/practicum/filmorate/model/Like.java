package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Like {
    private Long filmId;
    private Long userId;

    public Like(Long filmId, Long userId) {
        this.filmId = filmId;
        this.userId = userId;
    }
}
