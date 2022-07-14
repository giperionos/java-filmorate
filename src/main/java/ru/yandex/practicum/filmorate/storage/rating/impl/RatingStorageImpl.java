package ru.yandex.practicum.filmorate.storage.rating.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class RatingStorageImpl implements RatingStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RatingStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MPARating getById(Integer id) {
        String sqlQuery = "select * from RATINGS_MPA where RATING_ID = ?";
        List<MPARating> ratings = jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToRatingMPA(rs)), id);
        if (ratings.size() != 1) {
            throw new EntityNotFoundException(String.format("Сущность с %d не найдена в хранилище.", id));
        }
        return ratings.get(0);
    }

    @Override
    public List<MPARating> getAll() {
        String sqlQuery = "select * from RATINGS_MPA";
        return jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToRatingMPA(rs)));
    }

    public MPARating mapRowToRatingMPA(ResultSet rs) throws SQLException {
        return new MPARating(
                rs.getInt("RATING_ID"),
                rs.getString("RATING_NAME"),
                rs.getString("RATING_DESCRIPTION")
       );
    }
}
