package ru.yandex.practicum.filmorate.storage.friend;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFriend;

import java.util.List;

public interface UserFriendStorage {

    void addNewUserFriendLink(Long ownerUserId, Long friendId);

    boolean removeUserFriendLinkByUserIdAndFriendId(Long ownerUserId, Long friendId);

    List<User> getAllFriendsByOwnerUserId(Long ownerUserId);

    List<User> getCommonUserFriends(Long ownerId, Long otherId);

    List<UserFriend> getAllUserFriendLinks();

    UserFriend getUserFriendLinkByOwnerId(Long ownerId);
}
