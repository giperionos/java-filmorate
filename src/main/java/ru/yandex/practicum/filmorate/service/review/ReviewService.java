package ru.yandex.practicum.filmorate.service.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.usefulReview.UsefulReviewStorage;

import java.util.List;

@Service
@Slf4j
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final UsefulReviewStorage usefulReviewStorage;
    private final UserService userService;
    private final FilmService filmService;

    @Autowired
    public ReviewService(ReviewStorage reviewStorage, UsefulReviewStorage usefulReviewStorage, UserService userService, FilmService filmService) {
        this.reviewStorage = reviewStorage;
        this.usefulReviewStorage = usefulReviewStorage;
        this.userService = userService;
        this.filmService = filmService;
    }

    public Review addNewReview(Review review) {
        //перед добавлением проверить, что такой пользователь и фильм вообще есть
        try {
            userService.getUserById(review.getUserId());
        } catch (EntityNotFoundException e) {
            log.warn("В review пришел неизвестный пользователь.");
            throw e;
        }

        try {
            filmService.getFilmById(review.getFilmId());
        } catch (EntityNotFoundException e) {

            log.warn("В review пришел неизвестный фильм.");
            throw e;
        }

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
