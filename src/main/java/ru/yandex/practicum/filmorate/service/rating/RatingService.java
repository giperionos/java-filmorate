package ru.yandex.practicum.filmorate.service.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;

import java.util.List;

@Service
public class RatingService {

    private RatingStorage ratingStorage;

    @Autowired
    public RatingService(RatingStorage ratingStorage) {
        this.ratingStorage = ratingStorage;
    }

    public MPARating getByMPARatingId(Integer ratingMpaId){
        return ratingStorage.getRatingMpaById(ratingMpaId);
    }

    public List<MPARating> getAllMPARatings(){
        return ratingStorage.getAllRatingsMpa();
    }
}
