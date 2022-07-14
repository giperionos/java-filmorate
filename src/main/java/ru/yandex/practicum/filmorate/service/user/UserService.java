package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UnknownUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.UserFriendsStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserStorage userStorage;
    private final UserFriendsStorage userFriendsStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage")UserStorage userStorage, UserFriendsStorage userFriendsStorage) {
        this.userStorage = userStorage;
        this.userFriendsStorage = userFriendsStorage;
    }

    public User add(User user) {
        return userStorage.add(user);
    }

    public User update(User user) {
        try {
            return userStorage.update(user);
        } catch (EntityNotFoundException exception) {
            throw new UnknownUserException(String.format("Фильм с %d не найден в хранилище.", user.getId()));
        }
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User getUserById(Long id) {
        User foundedUser = userStorage.getById(id);

        if (foundedUser == null) {
            throw new EntityNotFoundException(String.format("Пользователь с %d не найден в хранилище.", id));
        }

        return foundedUser;
    }

    public void addToFriends(Long userId, Long friendId) {

        //Дружба должна стать односторонней.
        //Это значит, что если какой-то пользователь оставил вам заявку в друзья,
        //то он будет в списке ваших друзей, а вы в его — нет

        //сначала найден этих пользователей
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        //ошибка - если таких пользователей нет
        if (user == null) {
            throw new EntityNotFoundException(String.format("Пользователь с %d не найден в хранилище.", userId));
        }

        if (friend == null) {
            throw new EntityNotFoundException(String.format("Пользователь с %d не найден в хранилище.", friendId));
        }

        //добавить user-у в друзья пользователя с friendId
        userFriendsStorage.add(userId, friendId);
    }

    public void removeFromFriends(Long userId, Long friendId) {
        //удалить из друзей нужно их обоих друг у друга

        //сначала найден этих пользователей
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        //ошибка - если таких пользователей нет
        if (user == null) {
            throw new EntityNotFoundException(String.format("Пользователь с %d не найден в хранилище.", userId));
        }

        if (friend == null) {
            throw new EntityNotFoundException(String.format("Пользователь с %d не найден в хранилище.", friendId));
        }

        //удалить у user-а из друзей пользователя с friendId
        userFriendsStorage.remove(userId, friendId);
    }

    public List<User> getAllUserFriends(Long id) {
        User foundedUser = userStorage.getById(id);

        if (foundedUser == null) {
            throw new EntityNotFoundException(String.format("Пользователь с %d не найден в хранилище.", id));
        }

        //вернуть список всех друзей пользователя
        return userFriendsStorage.getAllFriendsByOwnerUserId(foundedUser.getId());
    }

    public List<User> getCommonUserFriends(Long id, Long otherId) {
        //список друзей, общих с другим пользователем
        return userFriendsStorage.getCommonUserFriends(id,otherId);
    }
}
