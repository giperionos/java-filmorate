package ru.yandex.practicum.filmorate.storage.rating;


import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.List;

public interface RatingStorage {
    MPARating getById(Integer id);
    List<MPARating> getAll();
}
