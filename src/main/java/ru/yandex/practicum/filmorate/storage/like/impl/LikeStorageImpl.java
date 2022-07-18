package ru.yandex.practicum.filmorate.storage.like.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class LikeStorageImpl implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(Long filmId, Long userId) {
        String sqlQuery = "insert into \"LIKE\" (FILM_ID, USER_ID) VALUES ( ?, ? );";

        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public boolean delete(Long filmId, Long userId) {
        String sqlQuery = "delete from \"LIKE\" where FILM_ID = ? and USER_ID = ?;";

        return jdbcTemplate.update(sqlQuery, filmId, userId) > 0;
    }

    @Override
    public List<Like> getAll() {
        String sqlQuery = "select * from \"LIKE\"";
        return jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToLikes(rs)));
    }

    @Override
    public boolean deleteAll() {
        String sqlQuery = "delete from \"LIKE\";";

        return jdbcTemplate.update(sqlQuery) > 0;
    }

    public Like mapRowToLikes (ResultSet rs) throws SQLException {
        return new Like(rs.getLong("FILM_ID"), rs.getLong("USER_ID"));
    }
}
