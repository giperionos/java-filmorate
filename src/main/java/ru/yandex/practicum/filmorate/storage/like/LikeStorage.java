package ru.yandex.practicum.filmorate.storage.like;

import ru.yandex.practicum.filmorate.model.Like;

import java.util.List;

public interface LikeStorage {
    void addNewLikeForFilmIdByUserId(Long filmId, Long userId);
    boolean deleteLikeForFilmIdByUserId(Long filmId, Long userId);
    List<Like> getAllLikes();
    boolean deleteAllLikes();
}
