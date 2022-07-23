package ru.yandex.practicum.filmorate.storage.usefulReview;

public interface UsefulReviewStorage {

    boolean addLikeReview(Long reviewId, Long userId);

    boolean addDislikeReview(Long reviewId, Long userId);

    boolean deleteLikeReview(Long reviewId, Long userId);

    boolean deleteDislikeReview(Long reviewId, Long userId);
}
