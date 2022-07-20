package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class UsefulReview {

        long reviewId;
        long userId;
        boolean like;

        public UsefulReview(long reviewId, long userId, boolean like) {
                this.reviewId = reviewId;
                this.userId = userId;
                this.like = like;
        }
}
