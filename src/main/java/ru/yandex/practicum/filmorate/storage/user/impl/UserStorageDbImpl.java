package ru.yandex.practicum.filmorate.storage.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UnknownUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

@Component
@Qualifier("userDbStorage")
public class UserStorageDbImpl implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserStorageDbImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addNewUser(User entity) {
        String sqlQuery = "insert into \"USER\" (EMAIL, LOGIN, USER_NAME, BIRTHDAY) VALUES ( ?, ?, ?, ? )";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, entity.getEmail());
            stmt.setString(2, entity.getLogin());
            stmt.setString(3, entity.getName());
            final LocalDate birthday = entity.getBirthday();
            if (entity.getBirthday() == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(entity.getBirthday().toString()));
            }

            return stmt;
        }, keyHolder);

        entity.setId(keyHolder.getKey().longValue());

        return entity;
    }

    @Override
    public User updateUser(User entity) {
        String sqlQuery = "update \"USER\" set EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ? where USER_ID = ?";

        int isUpdated = jdbcTemplate.update(sqlQuery
                , entity.getEmail()
                , entity.getLogin()
                , entity.getName()
                , entity.getBirthday()
                , entity.getId()
        );

        if (isUpdated == 0) {
            throw new UnknownUserException(String.format("Пользователь с %d не найден в таблице USER", entity.getId()));
        }

        return entity;
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "select USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY from \"USER\"";

        return jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToUser(rs)));
    }

    @Override
    public User getUserById(Long userId) {
        String sqlQuery = "select USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY from \"USER\" where USER_ID = ?";
        List<User> users = jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToUser(rs)), userId);

        if (users.size() != 1) {
            throw new UnknownUserException(String.format("Пользователь с %d не найден в таблице USER", userId));
        }

        return users.get(0);
    }


    public boolean deleteAll() {
        String sqlQuery = "delete from \"USER\" cascade;";

        return jdbcTemplate.update(sqlQuery) > 0;
    }

    public static User mapRowToUser(ResultSet rs) throws SQLException  {
        return new User(
            rs.getLong("USER_ID"),
            rs.getString("EMAIL"),
            rs.getString("LOGIN"),
            rs.getString("USER_NAME"),
            rs.getDate("BIRTHDAY") .toLocalDate()
        );
    }

    public void deleteUserById(Long userId) {
        String sqlQuery = "delete from \"USER\" where USER_ID = ?";
        if (jdbcTemplate.update(sqlQuery, userId) == 0) {
            throw new UnknownUserException(String.format("Сущность с userId=%d не найдена в таблице USER:", userId));
        }
    }
}
