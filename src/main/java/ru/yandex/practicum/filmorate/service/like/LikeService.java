package ru.yandex.practicum.filmorate.service.like;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UnknownFilmException;
import ru.yandex.practicum.filmorate.exceptions.UnknownUserException;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Service
@Slf4j
public class LikeService {

    private LikeStorage likeStorage;
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public LikeService(LikeStorage likeStorage, @Qualifier("filmStorageDbImpl") FilmStorage filmStorage, @Qualifier("userStorageDbImpl") UserStorage userStorage) {
        this.likeStorage = likeStorage;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Long filmId, Long userId) {
        //перед добавлением нужно проверить есть ли такие фильм и пользователь
        throwExceptionIfUserOrFilmNotExists(filmId, userId);

        likeStorage.addNewLikeForFilmIdByUserId(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        //перед удалением нужно проверить есть ли такие фильм и пользователь
        throwExceptionIfUserOrFilmNotExists(filmId, userId);

        boolean isDelete = likeStorage.deleteLikeForFilmIdByUserId(filmId, userId);

        if (!isDelete){
            throw  new EntityNotFoundException(
                    String.format("Лайк для фильма %d поставленный пользователем %d не удален." , filmId, userId)
            );
        }
    }

    private void throwExceptionIfUserOrFilmNotExists(Long filmId, Long userId) {

        try {
            filmStorage.getFilmById(filmId);
        } catch (UnknownFilmException e) {
            log.warn(String.format("Фильм с id = %d не найден в приложении." , filmId));
            throw e;
        }

        try {
            userStorage.getUserById(userId);
        } catch (UnknownUserException e) {
            log.warn(String.format("Пользователь с id = %d не найден в приложении." , userId));
            throw e;
        }
    }
}
