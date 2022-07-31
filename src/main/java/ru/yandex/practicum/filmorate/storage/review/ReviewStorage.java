package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {

    Review addNewReview(Review review);

    Review updateReview(Review review);

    void deleteReview(Long reviewId);

    Review getReviewById(Long reviewId);

    List<Review> getAllReviewes(Integer count);

    List<Review> getReviewesByFilmId(Integer count, Long filmId);

    void addLike(Long reviewId);

    void addDislike(Long reviewId);

    void deleteLike(Long reviewId);

    void deleteDislike(Long reviewId);
}
