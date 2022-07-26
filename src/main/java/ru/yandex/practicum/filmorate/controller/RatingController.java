package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.rating.RatingService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
public class RatingController {

    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping("/{ratingMpaId}")
    public MpaRating getByMpaRatingId(@PathVariable Integer ratingMpaId) {
        return ratingService.getByMpaRatingId(ratingMpaId);
    }

    @GetMapping
    public List<MpaRating> getAllMpaRatings() {
        return ratingService.getAllMpaRatings();
    }
}
