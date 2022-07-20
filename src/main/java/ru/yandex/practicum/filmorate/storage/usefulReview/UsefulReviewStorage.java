package ru.yandex.practicum.filmorate.storage.usefulReview;

public interface UsefulReviewStorage {
    //int getUseful(long id);

    boolean addLikeReview(Long id, Long userId);

    boolean addDislikeReview(Long id, Long userId);

    boolean deleteLikeReview(Long id, Long userId);

    boolean deleteDislikeReview(Long id, Long userId);
}
