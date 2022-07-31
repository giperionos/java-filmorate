package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@AllArgsConstructor
public class UsefulReviewEvent {

    private Long reviewId;
    private Long userId;
    private Boolean like;
}
