package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.review.ReviewService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public Review addNewReview(@Valid @RequestBody Review review){
        log.debug("Добавление POST запроса /reviews");
        return reviewService.addNewReview(review);
    }

    @PutMapping
    public Review updateReview (@Valid @RequestBody Review review) {
        log.debug("Добавление PUT запроса /reviews");
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id);
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

    @PutMapping ("/{id}/like/{userId}")
    public void addLikeReview(@PathVariable Long id, @PathVariable Long userId) {
        reviewService.addLikeReview(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void addDislikeReview(@PathVariable Long id, @PathVariable Long userId) {
        reviewService.addDislikeReview(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void  deleteLikeReview(@PathVariable Long id, @PathVariable Long userId){
        reviewService.deleteLikeReview(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void  deleteDislikeReview(@PathVariable Long id, @PathVariable Long userId){
        reviewService.deleteDislikeReview(id, userId);
    }

}
