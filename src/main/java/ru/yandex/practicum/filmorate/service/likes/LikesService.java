package ru.yandex.practicum.filmorate.service.likes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UnknownFilmException;
import ru.yandex.practicum.filmorate.exceptions.UnknownUserException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikesStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Service
public class LikesService {
    private LikesStorage likesStorage;
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public LikesService(LikesStorage likesStorage, @Qualifier("filmDbStorage") FilmStorage filmStorage, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.likesStorage = likesStorage;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Long filmId, Long userId) {
        //перед добавлением нужно проверить есть ли такие фильм и пользователь
        throwExceptionIfUserOrFilmNotExists(filmId, userId);

        likesStorage.add(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        //перед удалением нужно проверить есть ли такие фильм и пользователь
        throwExceptionIfUserOrFilmNotExists(filmId, userId);

        boolean isDelete = likesStorage.delete(filmId, userId);

        if (!isDelete){
            throw  new EntityNotFoundException(
                    String.format("Лайк для фильма %d поставленный пользователем %d не удален." , filmId, userId)
            );
        }
    }

    private void throwExceptionIfUserOrFilmNotExists(Long filmId, Long userId){
        Film film = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);

        if (film == null) {
            throw new UnknownFilmException(
                    String.format("Фильм с id = %d не найден в приложении." , filmId)
            );
        }

        if (user == null) {
            throw new UnknownUserException(
                    String.format("Пользователь с id = %d не найден в приложении." , userId)
            );
        }
    }
}
