package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UsefulReviewEvent {

    private Long reviewId;
    private Long userId;
    private Boolean like;

    public UsefulReviewEvent(Long reviewId, Long userId, Boolean like) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.like = like;
    }
}
