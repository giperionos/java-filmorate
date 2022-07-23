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
        log.debug("Добавление POST запроса /reviews");
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
        log.debug("Добавление PUT запроса /reviews");
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
    }

    @GetMapping("/{reviewId}")
    public Review getReviewById(@PathVariable Long reviewId) {
        return reviewService.getReviewById(reviewId);
    }

    @GetMapping
    public List<Review> getAllReviewes(@RequestParam (defaultValue = "10") Integer count,
                                        @RequestParam (required = false) Long filmId) {
        if(filmId == null){
            return reviewService.getAllReviewes(count);
        } else {
            return reviewService.getReviewesByFilmId(count, filmId);
        }
    }

    @PutMapping ("/{reviewId}/like/{userId}")
    public void addLikeReview(@PathVariable Long reviewId, @PathVariable Long userId) {
        reviewService.addLikeReview(reviewId, userId);
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public void addDislikeReview(@PathVariable Long reviewId, @PathVariable Long userId) {
        reviewService.addDislikeReview(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/like/{userId}")
    public void  deleteLikeReview(@PathVariable Long reviewId, @PathVariable Long userId){
        reviewService.deleteLikeReview(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/dislike/{userId}")
    public void  deleteDislikeReview(@PathVariable Long reviewId, @PathVariable Long userId){
        reviewService.deleteDislikeReview(reviewId, userId);
    }
}
