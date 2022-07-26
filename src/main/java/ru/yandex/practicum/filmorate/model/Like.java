package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@AllArgsConstructor
public class Like {

    private Long filmId;
    private Long userId;
}
