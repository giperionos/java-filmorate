package ru.yandex.practicum.filmorate.service.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;

import java.util.List;

@Service
public class RatingService {

    private RatingStorage ratingStorage;

    @Autowired
    public RatingService(RatingStorage ratingStorage) {
        this.ratingStorage = ratingStorage;
    }

    public MpaRating getByMpaRatingId(Integer ratingMpaId){
        return ratingStorage.getRatingMpaById(ratingMpaId);
    }

    public List<MpaRating> getAllMpaRatings(){
        return ratingStorage.getAllRatingsMpa();
    }
}
