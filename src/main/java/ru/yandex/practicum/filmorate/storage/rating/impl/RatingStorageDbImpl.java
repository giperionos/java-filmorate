package ru.yandex.practicum.filmorate.storage.rating.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class RatingStorageDbImpl implements RatingStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RatingStorageDbImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MpaRating getRatingMpaById(Integer ratingMpaId) {
        String sqlQuery = "select * from RATING_MPA where RATING_ID = ?";
        List<MpaRating> ratings = jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToRatingMPA(rs)), ratingMpaId);
        if (ratings.size() != 1) {
            throw new EntityNotFoundException(String.format("Сущность с %d не найдена в хранилище.", ratingMpaId));
        }
        return ratings.get(0);
    }

    @Override
    public List<MpaRating> getAllRatingsMpa() {
        String sqlQuery = "select * from RATING_MPA";
        return jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToRatingMPA(rs)));
    }

    public MpaRating mapRowToRatingMPA(ResultSet rs) throws SQLException {
        return new MpaRating(
                rs.getInt("RATING_ID"),
                rs.getString("RATING_NAME"),
                rs.getString("RATING_DESCRIPTION")
       );
    }
}
