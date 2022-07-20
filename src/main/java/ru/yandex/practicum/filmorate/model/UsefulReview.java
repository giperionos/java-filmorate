package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UsefulReview {

    private Long reviewId;
    private Long userId;
    private Boolean like;

    public UsefulReview(Long reviewId, Long userId, Boolean like) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.like = like;
    }
}
