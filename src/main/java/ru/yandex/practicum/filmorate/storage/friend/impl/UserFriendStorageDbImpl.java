package ru.yandex.practicum.filmorate.storage.friend.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFriend;
import ru.yandex.practicum.filmorate.storage.friend.UserFriendStorage;
import ru.yandex.practicum.filmorate.storage.user.impl.UserStorageDbImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class UserFriendStorageDbImpl implements UserFriendStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserFriendStorageDbImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addNewUserFriendLink(Long ownerUserId, Long friendId) {
        String sqlQuery = "insert into USER_FRIEND (OWNER_USER_ID, FRIEND_ID, RELATION_SHIP_STATUS) VALUES ( ?, ?, ?)";

        jdbcTemplate.update(sqlQuery,ownerUserId, friendId, "ACCEPTED");
    }

    @Override
    public boolean removeUserFriendLinkByUserIdAndFriendId(Long ownerUserId, Long friendId) {
        String sqlQuery = "delete from USER_FRIEND where OWNER_USER_ID = ? and FRIEND_ID = ?";
        return jdbcTemplate.update(sqlQuery, ownerUserId, friendId) > 0;
    }

    @Override
    public List<User> getAllFriendsByOwnerUserId(Long ownerUserId) {
        String sqlQuery = "select * from \"USER\" "
               + "LEFT JOIN USER_FRIEND UF on USER_ID = UF.FRIEND_ID "
               + "where UF.OWNER_USER_ID = ?;";

        return jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> UserStorageDbImpl.mapRowToUser(rs)), ownerUserId);
    }

    @Override
    public List<User> getCommonUserFriends(Long ownerId, Long otherId) {
        String sqlQuery =
                "select * from \"USER\" where USER_ID in (\n" +
                "    select \n" +
                "        uf1.FRIEND_ID \n" +
                "    from USER_FRIEND uf1\n" +
                "    left outer join USER_FRIEND uf2 on uf1.FRIEND_ID=uf2.FRIEND_ID\n" +
                "    where uf1.OWNER_USER_ID = ? and uf2.OWNER_USER_ID= ?\n" +
                ");";


        return jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> UserStorageDbImpl.mapRowToUser(rs)), ownerId, otherId);
    }

    @Override
    public List<UserFriend> getAllUserFriendLinks() {
        String sqlQuery = "select * from USER_FRIEND";
        return jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToUserFriend(rs)));
    }

    @Override
    public UserFriend getUserFriendLinkByOwnerId(Long ownerId) {
        String sqlQuery = "select * from USER_FRIEND where OWNER_USER_ID = ?";
        List<UserFriend> userFriends = jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToUserFriend(rs)), ownerId);

        if (userFriends.size() != 1) {
            throw new EntityNotFoundException(String.format("Сущность с %d не найдена в таблице USER:", ownerId));
        }

        return userFriends.get(0);
    }

    public UserFriend mapRowToUserFriend (ResultSet rs) throws SQLException {
        return new UserFriend(
                rs.getLong("OWNER_USER_ID"),
                rs.getLong("FRIEND_ID"),
                rs.getString("RELATION_SHIP_STATUS")
        );
    }
}
