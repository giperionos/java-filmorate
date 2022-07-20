package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {

    Review addNewReview(Review review);

    Review updateReview(Review review);

    void deleteReview(long id);

    Review getReviewById(long id);

    List<Review> getAllReviewes(Integer count);

    List<Review> getReviewesByFilmId(Integer count, Long filmId);

    void addLike(Long id);

    void addDislike(Long id);

    void deleteLike(Long id);

    void deleteDislike(Long id);
}
