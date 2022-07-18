package ru.yandex.practicum.filmorate.storage.friends.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.config.FilmorateConfig;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFriend;
import ru.yandex.practicum.filmorate.storage.friend.impl.UserFriendStorageImpl;
import ru.yandex.practicum.filmorate.storage.user.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserFriendStorageImplTest {

    private final UserFriendStorageImpl userFriendsStorage;
    private final UserDbStorage userDbStorage;
    private static User userFirst;
    private static User userSecond;
    private static User userJohn;
    private static User userLeo;
    private static User userNeo;
    private static User userSmith;

    @BeforeAll
    public static void init(){
        userFirst = new User(null, "first@test.ru", "first login", "first name",  LocalDate.parse("14.10.1999", FilmorateConfig.normalDateFormatter));
        userSecond = new User(null, "second@test.ru", "second login", "second name",  LocalDate.parse("14.10.1999", FilmorateConfig.normalDateFormatter));
        userJohn = new User(null, "John@test.ru", "John login", "John name",  LocalDate.parse("14.10.1999", FilmorateConfig.normalDateFormatter));
        userLeo = new User(null, "Leo@test.ru", "Leo login", "Leo name",  LocalDate.parse("14.10.1999", FilmorateConfig.normalDateFormatter));
        userNeo = new User(null, "Neo@test.ru", "Neo login", "Neo name",  LocalDate.parse("14.10.1999", FilmorateConfig.normalDateFormatter));
        userSmith = new User(null, "Smith@test.ru", "Smith login", "Smith name",  LocalDate.parse("14.10.1999", FilmorateConfig.normalDateFormatter));
    }

    @Test
    void testAddUser() {

        //Сначала нужно добавить самих пользователей
        userDbStorage.add(userFirst);
        userDbStorage.add(userSecond);

        //затем добавить дружбу
        userFriendsStorage.add(userFirst.getId(), userSecond.getId());

        UserFriend userFriend = userFriendsStorage.getByOwnerId(userFirst.getId());

        assertEquals(userFirst.getId(), userFriend.getOwnerUserId(), "Id добавленного пользователя не совпадает.");
        assertEquals(userSecond.getId(), userFriend.getFriendId(), "Id добавленного друга не совпадает.");
    }

    @Test
    void testRemoveUser() {
        //Сначала нужно добавить самих пользователей
        userDbStorage.add(userFirst);
        userDbStorage.add(userSecond);

        //затем добавить дружбу
        userFriendsStorage.add(userFirst.getId(), userSecond.getId());

        boolean isDelete = userFriendsStorage.remove(userFirst.getId(), userSecond.getId());
        assertTrue(isDelete, "Удаление из таблицы не случилось.");
    }

    @Test
    void getAllFriendsByOwnerUserId() {
        //Сначала нужно добавить самих пользователей
        userDbStorage.add(userJohn);
        userDbStorage.add(userLeo);
        userDbStorage.add(userNeo);
        userDbStorage.add(userSmith);

        //затем добавить дружбу
        //У Neo в друзьях Leo и John
        userFriendsStorage.add(userNeo.getId(), userLeo.getId());
        userFriendsStorage.add(userNeo.getId(), userJohn.getId());

        //У John в друзях Smith
        userFriendsStorage.add(userJohn.getId(), userSmith.getId());

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
        userDbStorage.add(userJohn);
        userDbStorage.add(userLeo);
        userDbStorage.add(userNeo);
        userDbStorage.add(userSmith);

        //затем добавить дружбу
        //У Neo в друзьях Leo и John
        userFriendsStorage.add(userNeo.getId(), userLeo.getId());
        userFriendsStorage.add(userNeo.getId(), userJohn.getId());

        //У John в друзьях Leo и Smith
        userFriendsStorage.add(userJohn.getId(), userLeo.getId());
        userFriendsStorage.add(userJohn.getId(), userSmith.getId());

        //список друзей, общих с другим пользователем
        //У Neo и John 1 общий друг Leo
        List<User> commonFriends = userFriendsStorage.getCommonUserFriends(userNeo.getId(), userJohn.getId());

        assertEquals(1, commonFriends.size(), "Количество общих друзей  не совпало с ожидаемым.");
        assertTrue(commonFriends.contains(userLeo));
    }
}