package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Review {

    private Long reviewId;

    @NotBlank(message = "Отзыв не должно быть пустым.")
    private String content;

    @NotNull
    private Boolean isPositive;

    @NotNull
    private Long userId;

    @NotNull
    private Long filmId;

    private int useful = 0;

    public Review(Long reviewId, String content, Boolean isPositive,
                  Long userId,  Long filmId, int useful) {
        this.reviewId = reviewId;
        this.content = content;
        this.isPositive = isPositive;
        this.userId = userId;
        this.filmId = filmId;
        this.useful = useful;
    }

    public Review() {
    }

   /* public boolean getIsPositive() {
        return this.isPositive;
    }

    public void setIsPositive(boolean isPositive) {
        this.isPositive = isPositive;
    }*/
}
