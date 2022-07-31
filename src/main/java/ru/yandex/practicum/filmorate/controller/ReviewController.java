package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.event.EventService;
import ru.yandex.practicum.filmorate.service.review.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;
    private final EventService eventService;

    @Autowired
    public ReviewController(ReviewService reviewService, EventService eventService) {
        this.reviewService = reviewService;
        this.eventService = eventService;
    }

    @PostMapping
    public Review addNewReview(@Valid @RequestBody Review review){
        final Review addedReview = reviewService.addNewReview(review);
        log.debug("Добавлено новое ревью. Id: {}", review.getReviewId());
        eventService.add(new Event(
                addedReview.getReviewId(),
                addedReview.getUserId(),
                EventType.REVIEW,
                Operation.ADD)
        );

        return addedReview;
    }

    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review) {
        final Review updatedReview = reviewService.updateReview(review);
        log.debug("Обновлено ревью. Id: {}", review.getReviewId());
        eventService.add(new Event(
                updatedReview.getReviewId(),
                updatedReview.getUserId(),
                EventType.REVIEW,
                Operation.UPDATE)
        );
        return updatedReview;
    }

    @DeleteMapping("/{reviewId}")
    public void deleteReviewById(@PathVariable Long reviewId) {
        eventService.add(new Event(
                reviewId,
                reviewService.getReviewById(reviewId).getUserId(),
                EventType.REVIEW,
                Operation.REMOVE)
        );

        reviewService.deleteReviewById(reviewId);
        log.debug("Удалено ревью. Id: {}", reviewId);
    }

    @GetMapping("/{reviewId}")
    public Review getReviewById(@PathVariable Long reviewId) {
        return reviewService.getReviewById(reviewId);
    }

    @GetMapping
    public List<Review> getReviews(@RequestParam(defaultValue = "10") Integer count,
                                   @RequestParam(required = false) Long filmId) {
        if(filmId == null){
            return reviewService.getAllReviewes(count);
        } else {
            return reviewService.getReviewesByFilmId(count, filmId);
        }
    }

    @PutMapping ("/{reviewId}/like/{userId}")
    public void addLikeReview(@PathVariable Long reviewId, @PathVariable Long userId) {
        reviewService.addLikeReview(reviewId, userId);
        log.debug("Довавлен like к ревью. Id: {}", reviewId);
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public void addDislikeReview(@PathVariable Long reviewId, @PathVariable Long userId) {
        reviewService.addDislikeReview(reviewId, userId);
        log.debug("Довавлен dislike к ревью. Id: {}", reviewId);
    }

    @DeleteMapping("/{reviewId}/like/{userId}")
    public void  deleteLikeReview(@PathVariable Long reviewId, @PathVariable Long userId){
        reviewService.deleteLikeReview(reviewId, userId);
        log.debug("Удален like к ревью. Id: {}", reviewId);
    }

    @DeleteMapping("/{reviewId}/dislike/{userId}")
    public void  deleteDislikeReview(@PathVariable Long reviewId, @PathVariable Long userId){
        reviewService.deleteDislikeReview(reviewId, userId);
        log.debug("Удален dislike к ревью. Id: {}", reviewId);
    }
}
