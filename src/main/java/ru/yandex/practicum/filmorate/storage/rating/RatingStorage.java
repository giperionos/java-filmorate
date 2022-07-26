package ru.yandex.practicum.filmorate.storage.rating;


import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

public interface RatingStorage {

    MpaRating getRatingMpaById(Integer ratingMpaId);

    List<MpaRating> getAllRatingsMpa();
}
