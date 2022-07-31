package ru.yandex.practicum.filmorate.storage.friends.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.config.FilmorateConfig;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFriend;
import ru.yandex.practicum.filmorate.storage.friend.impl.UserFriendStorageDbImpl;
import ru.yandex.practicum.filmorate.storage.user.impl.UserStorageDbImpl;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserFriendStorageDbImplTest {

    private final UserFriendStorageDbImpl userFriendsStorage;
    private final UserStorageDbImpl userDbStorage;

    private Long user_id_null = null;
    private User userFirst = new User(user_id_null, "first@test.ru", "first login", "first name",
            LocalDate.parse("14.10.1999", FilmorateConfig.NORMAL_DATE_FORMATTER));
    private User userSecond = new User(user_id_null, "second@test.ru", "second login", "second name",
            LocalDate.parse("14.10.1999", FilmorateConfig.NORMAL_DATE_FORMATTER));
    private User userJohn = new User(user_id_null, "John@test.ru", "John login", "John name",
            LocalDate.parse("14.10.1999", FilmorateConfig.NORMAL_DATE_FORMATTER));
    private User userLeo = new User(user_id_null, "Leo@test.ru", "Leo login", "Leo name",
            LocalDate.parse("14.10.1999", FilmorateConfig.NORMAL_DATE_FORMATTER));
    private User userNeo = new User(user_id_null, "Neo@test.ru", "Neo login", "Neo name",
            LocalDate.parse("14.10.1999", FilmorateConfig.NORMAL_DATE_FORMATTER));
    private User userSmith = new User(user_id_null, "Smith@test.ru", "Smith login", "Smith name",
            LocalDate.parse("14.10.1999", FilmorateConfig.NORMAL_DATE_FORMATTER));

    @Test
    void testAddUser() {
        //Сначала нужно добавить самих пользователей
        userDbStorage.addNewUser(userFirst);
        userDbStorage.addNewUser(userSecond);

        //затем добавить дружбу
        userFriendsStorage.addNewUserFriendLink(userFirst.getId(), userSecond.getId());

        UserFriend userFriend = userFriendsStorage.getUserFriendLinkByOwnerId(userFirst.getId());

        assertEquals(userFirst.getId(), userFriend.getOwnerUserId(), "Id добавленного пользователя не совпадает.");
        assertEquals(userSecond.getId(), userFriend.getFriendId(), "Id добавленного друга не совпадает.");
    }

    @Test
    void testRemoveUser() {
        //Сначала нужно добавить самих пользователей
        userDbStorage.addNewUser(userFirst);
        userDbStorage.addNewUser(userSecond);

        //затем добавить дружбу
        userFriendsStorage.addNewUserFriendLink(userFirst.getId(), userSecond.getId());

        boolean isDelete = userFriendsStorage.removeUserFriendLinkByUserIdAndFriendId(userFirst.getId(), userSecond.getId());
        assertTrue(isDelete, "Удаление из таблицы не случилось.");
    }

    @Test
    void getAllFriendsByOwnerUserId() {
        //Сначала нужно добавить самих пользователей
        userDbStorage.addNewUser(userJohn);
        userDbStorage.addNewUser(userLeo);
        userDbStorage.addNewUser(userNeo);
        userDbStorage.addNewUser(userSmith);

        //затем добавить дружбу
        //У Neo в друзьях Leo и John
        userFriendsStorage.addNewUserFriendLink(userNeo.getId(), userLeo.getId());
        userFriendsStorage.addNewUserFriendLink(userNeo.getId(), userJohn.getId());

        //У John в друзях Smith
        userFriendsStorage.addNewUserFriendLink(userJohn.getId(), userSmith.getId());

        //получить друзей Neo
        List<User> friendsNeo =  userFriendsStorage.getAllFriendsByOwnerUserId(userNeo.getId());

        //получить друзей John
        List<User> friendsJohn =  userFriendsStorage.getAllFriendsByOwnerUserId(userJohn.getId());

        //у Neo двое друзей
        assertEquals(2, friendsNeo.size(), "Количество друзей пользователя не совпало с ожидаемым.");

        //У Neo в друзьях Leo и John
        assertTrue(friendsNeo.containsAll(List.of(userLeo, userJohn)));

        //У John в друзях Smith
        assertTrue(friendsJohn.contains(userSmith));
    }

    @Test
    void getCommonUserFriends() {
        //Сначала нужно добавить самих пользователей
        userDbStorage.addNewUser(userJohn);
        userDbStorage.addNewUser(userLeo);
        userDbStorage.addNewUser(userNeo);
        userDbStorage.addNewUser(userSmith);

        //затем добавить дружбу
        //У Neo в друзьях Leo и John
        userFriendsStorage.addNewUserFriendLink(userNeo.getId(), userLeo.getId());
        userFriendsStorage.addNewUserFriendLink(userNeo.getId(), userJohn.getId());

        //У John в друзьях Leo и Smith
        userFriendsStorage.addNewUserFriendLink(userJohn.getId(), userLeo.getId());
        userFriendsStorage.addNewUserFriendLink(userJohn.getId(), userSmith.getId());

        //список друзей, общих с другим пользователем
        //У Neo и John 1 общий друг Leo
        List<User> commonFriends = userFriendsStorage.getCommonUserFriends(userNeo.getId(), userJohn.getId());

        assertEquals(1, commonFriends.size(), "Количество общих друзей  не совпало с ожидаемым.");
        assertTrue(commonFriends.contains(userLeo));
    }
}
