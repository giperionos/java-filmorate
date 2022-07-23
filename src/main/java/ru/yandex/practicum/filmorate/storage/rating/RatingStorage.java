package ru.yandex.practicum.filmorate.storage.rating;


import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.List;

public interface RatingStorage {
    MPARating getRatingMpaById(Integer ratingMpaId);
    List<MPARating> getAllRatingsMpa();
}
