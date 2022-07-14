package ru.yandex.practicum.filmorate.storage.friends;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFriends;

import java.util.List;

public interface UserFriendsStorage {
    void add(Long ownerUserId, Long friendId);
    boolean remove(Long ownerUserId, Long friendId);
    List<User> getAllFriendsByOwnerUserId(Long id);
    List<User> getCommonUserFriends(Long ownerId, Long otherId);
    List<UserFriends> getAll();
    UserFriends getByOwnerId(Long id);
}
