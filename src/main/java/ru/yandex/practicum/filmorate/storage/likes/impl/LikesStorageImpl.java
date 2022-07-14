package ru.yandex.practicum.filmorate.storage.likes.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Likes;
import ru.yandex.practicum.filmorate.storage.likes.LikesStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class LikesStorageImpl implements LikesStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikesStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(Long filmId, Long userId) {
        String sqlQuery = "insert into LIKES (FILM_ID, USER_ID) VALUES ( ?, ? );";

        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public boolean delete(Long filmId, Long userId) {
        String sqlQuery = "delete from LIKES where FILM_ID = ? and USER_ID = ?;";

        return jdbcTemplate.update(sqlQuery, filmId, userId) > 0;
    }

    @Override
    public List<Likes> getAll() {
        String sqlQuery = "select * from LIKES";
        return jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToLikes(rs)));
    }

    @Override
    public boolean deleteAll() {
        String sqlQuery = "delete from LIKES;";

        return jdbcTemplate.update(sqlQuery) > 0;
    }

    public Likes mapRowToLikes (ResultSet rs) throws SQLException {
        return new Likes(rs.getLong("FILM_ID"), rs.getLong("USER_ID"));
    }
}
