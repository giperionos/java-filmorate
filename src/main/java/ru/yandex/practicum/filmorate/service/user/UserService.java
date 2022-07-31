package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UnknownUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.UserFriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;
    private final UserFriendStorage userFriendStorage;

    @Autowired
    public UserService(@Qualifier("userStorageDbImpl")UserStorage userStorage, UserFriendStorage userFriendStorage) {
        this.userStorage = userStorage;
        this.userFriendStorage = userFriendStorage;
    }

    public User addNewUser(User user) {
        return userStorage.addNewUser(user);
    }

    public User updateUser(User user) {
        try {
            return userStorage.updateUser(user);
        } catch (UnknownUserException exception) {
            log.warn(String.format("Фильм с %d не найден в хранилище.", user.getId()));
            throw exception;
        }
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Long userId) {

        User foundedUser;

        try {
            foundedUser = userStorage.getUserById(userId);
        } catch (UnknownUserException e) {
            log.warn("Пришел неизвестный пользователь с userId = " + userId);
            throw e;
        }

        return foundedUser;
    }

    public void addToFriends(Long userId, Long friendId) {

        try {
            userStorage.getUserById(userId);
        } catch (UnknownUserException e) {
            log.warn("Пришел неизвестный пользователь с userId = " + userId);
            throw e;
        }

        try {
            userStorage.getUserById(friendId);
        } catch (UnknownUserException e) {
            log.warn("Пришел неизвестный пользователь с userId = " + friendId);
            throw e;
        }

        //добавить user-у в друзья пользователя с friendId
        userFriendStorage.addNewUserFriendLink(userId, friendId);
    }

    public void removeFromFriends(Long userId, Long friendId) {
        //удалить из друзей нужно их обоих друг у друга

        try {
            userStorage.getUserById(userId);
        } catch (UnknownUserException e) {
            log.warn("Пришел неизвестный пользователь с userId = " + userId);
            throw e;
        }

        try {
            userStorage.getUserById(friendId);
        } catch (UnknownUserException e) {
            log.warn("Пришел неизвестный пользователь с userId = " + friendId);
            throw e;
        }

        //удалить у user-а из друзей пользователя с friendId
        userFriendStorage.removeUserFriendLinkByUserIdAndFriendId(userId, friendId);
    }

    public List<User> getAllUserFriendsByUserId(Long userId) {
        User foundedUser = userStorage.getUserById(userId);

        if (foundedUser == null) {
            throw new EntityNotFoundException(String.format("Пользователь с %d не найден в хранилище.", userId));
        }

        //вернуть список всех друзей пользователя
        return userFriendStorage.getAllFriendsByOwnerUserId(foundedUser.getId());
    }

    public List<User> getCommonUserFriends(Long userId, Long otherId) {
        //список друзей, общих с другим пользователем
        return userFriendStorage.getCommonUserFriends(userId,otherId);
    }

    public void removeUserById(Long userId) {
        userStorage.deleteUserById(userId);
    }
}
