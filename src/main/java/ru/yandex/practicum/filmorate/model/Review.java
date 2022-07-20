package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Review {

    private long reviewId;
    @NotBlank(message = "Отзыв не должно быть пустым.")
    private String content;
    @NonNull
    private Boolean isPositive;
    @NonNull
    private Long userId;
    @NonNull
    private Long filmId;
    private int useful = 0;

    public Review(long reviewId, String content, boolean isPositive,
                  @NonNull long userId, @NonNull long filmId, int useful) {
        this.reviewId = reviewId;
        this.content = content;
        this.isPositive = isPositive;
        this.userId = userId;
        this.filmId = filmId;
        this.useful = useful;
    }

    public Review() {
    }

    public boolean getIsPositive() {
        return this.isPositive;
    }

    public void setIsPositive(boolean isPositive) {
        this.isPositive = isPositive;
    }
}
