package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UnknownUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
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
        //То есть если Лена стала другом Саши, то это значит, что Саша теперь друг Лены
        //значит добавить в друзья нужно их обоих друг другу

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
        user.getFriends().add(friendId);

        //и наоборот
        friend.getFriends().add(userId);
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
        user.getFriends().remove(friendId);

        //и наоборот
        friend.getFriends().remove(userId);
    }

    public List<User> getAllUserFriends(Long id) {
        User foundedUser = userStorage.getById(id);

        if (foundedUser == null) {
            throw new EntityNotFoundException(String.format("Пользователь с %d не найден в хранилище.", id));
        }

        //вернуть список всех друзей пользователя
        List<User> users = new ArrayList<>();

        for(Long key: foundedUser.getFriends()){
            users.add(userStorage.getById(key));
        }

        return users;
    }

    public List<User> getCommonUserFriends(Long id, Long otherId) {
        //список друзей, общих с другим пользователем

        //список друзей первого пользователя
        List<User> oneUserFriends = getAllUserFriends(id);
        List<User> anotherUserFriends = getAllUserFriends(otherId);
        List<User> commonUserFriends = new ArrayList<>();

        for (User friend: oneUserFriends) {
            if (anotherUserFriends.contains(friend)) {
                commonUserFriends.add(friend);
            }
        }

        return commonUserFriends;
    }
}
