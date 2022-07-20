package ru.yandex.practicum.filmorate.service.review;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.usefulReview.UsefulReviewStorage;

import java.util.List;

@Service
public class ReviewService {

    ReviewStorage reviewStorage;
    UsefulReviewStorage usefulReviewStorage;

    public ReviewService(ReviewStorage reviewStorage, UsefulReviewStorage usefulReviewStorage) {
        this.reviewStorage = reviewStorage;
        this.usefulReviewStorage = usefulReviewStorage;
    }

    public Review addNewReview(Review review) {
        return reviewStorage.addNewReview(review);
    }

    public Review updateReview(Review review) {
        return reviewStorage.updateReview(review);
    }

    public void deleteReview(long id) {
        reviewStorage.deleteReview(id);
    }

    public Review getReviewById(long id) {
        return reviewStorage.getReviewById(id);
    }

    public List<Review> getAllReviewes(Integer count) {
        return reviewStorage.getAllReviewes(count);
    }

    public List<Review> getReviewesByFilmId(Integer count, Long filmId) {
        return reviewStorage.getReviewesByFilmId(count, filmId);
    }

    public void addLikeReview(Long id, Long userId) {
        if (usefulReviewStorage.addLikeReview(id, userId)){
            reviewStorage.addLike(id);
        }
    }

    public void addDislikeReview(Long id, Long userId) {
        if (usefulReviewStorage.addDislikeReview(id, userId)){
            reviewStorage.addDislike(id);
        }
    }

    public void deleteLikeReview(Long id, Long userId) {
        if (usefulReviewStorage.deleteLikeReview(id, userId)){
            reviewStorage.deleteLike(id);
        }
    }

    public void deleteDislikeReview(Long id, Long userId) {
        if (usefulReviewStorage.deleteDislikeReview(id, userId)){
            reviewStorage.deleteDislike(id);
        }
    }
}
