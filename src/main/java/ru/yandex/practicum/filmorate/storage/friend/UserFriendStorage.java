package ru.yandex.practicum.filmorate.storage.friend;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFriend;

import java.util.List;

public interface UserFriendStorage {
    void add(Long ownerUserId, Long friendId);
    boolean remove(Long ownerUserId, Long friendId);
    List<User> getAllFriendsByOwnerUserId(Long id);
    List<User> getCommonUserFriends(Long ownerId, Long otherId);
    List<UserFriend> getAll();
    UserFriend getByOwnerId(Long id);
}
