package ru.yandex.practicum.filmorate.storage.review.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class ReviewDBStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Review addNewReview(Review review){
            String sqlQuery = "insert into REVIEW (REVIEW_CONTENT, IS_POSITIVE, USER_ID, FILM_ID, USEFUL) " +
                    "values (?, ?, ?, ?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            int isUpdated = jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"REVIEW_ID"});
                stmt.setString(1, review.getContent());
                stmt.setBoolean(2, review.getIsPositive());
                stmt.setLong(3, review.getUserId());
                stmt.setLong(4, review.getFilmId());
                stmt.setInt(5, 0);
                return stmt;
            }, keyHolder);


            review.setReviewId(keyHolder.getKey().longValue());
            return review;


    }

    @Override
    public Review updateReview(Review review) {
        String sqlQuery = "update REVIEW set REVIEW_CONTENT = ?, IS_POSITIVE = ?, /*USER_ID = ?, FILM_ID = ?,*/ USEFUL = ? "
                + " where REVIEW_ID = ?";

        int isUpdated = jdbcTemplate.update(sqlQuery,
                review.getContent(),
                review.getIsPositive(),
                //review.getUserId(),
                //review.getFilmId(),
                0,
                review.getReviewId()
        );

        if (isUpdated == 0) {
            throw new EntityNotFoundException(String.format("Сущность с id %d не найдена в хранилище.",
                    review.getReviewId()));
        }

        return review;
    }

    @Override
    public void deleteReview(long id) {
        String sqlQuery = "delete from REVIEW where REVIEW_ID = ?";

        boolean deleting = jdbcTemplate.update(sqlQuery, id) > 0;
        if (!deleting){
            throw new EntityNotFoundException(String.format("Сущность с id %d не найдена в таблице REVIEW:", id));
        }
    }

    @Override
    public Review getReviewById(long id) {
        String sqlQuery = "select * from REVIEW where REVIEW_ID = ?";
        List<Review> reviewes = jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> makeReview(rs)), id);
        if (reviewes.size() != 1) {
            throw new EntityNotFoundException(String.format("Сущность с %d не найдена в таблице REVIEW:", id));
        }
        return reviewes.get(0);
    }

    @Override
    public List<Review> getAllReviewes(Integer count) {
        String sqlQuery = "select * from REVIEW order by USEFUL desc limit ?";
        return jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> makeReview(rs)), count);
    }

    @Override
    public List<Review> getReviewesByFilmId(Integer count, Long filmId) {
        String sqlQuery = "select * from REVIEW where FILM_ID = ? order by USEFUL desc limit ?";
        return jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> makeReview(rs)), filmId, count);
    }

    @Override
    public void addLike(Long id) {
        String sqlQuery = "update REVIEW set USEFUL = USEFUL + ? "
                + " where REVIEW_ID = ?";

        int isUpdated = jdbcTemplate.update(sqlQuery,1, id);
    }

    @Override
    public void addDislike(Long id) {
        String sqlQuery = "update REVIEW set USEFUL = USEFUL - ? "
                + " where REVIEW_ID = ?";

        jdbcTemplate.update(sqlQuery,1, id);
    }

    @Override
    public void deleteLike(Long id) {
        String sqlQuery = "update REVIEW set USEFUL = USEFUL - ? "
                + " where REVIEW_ID = ?";

        jdbcTemplate.update(sqlQuery,1, id);
    }

    @Override
    public void deleteDislike(Long id) {
        String sqlQuery = "update REVIEW set USEFUL = USEFUL + ? "
                + " where REVIEW_ID = ?";

        jdbcTemplate.update(sqlQuery,1, id);
    }

    private Review makeReview(ResultSet rs) throws SQLException {
        return new Review(
                rs.getLong("REVIEW_ID"),
                rs.getString("REVIEW_CONTENT"),
                rs.getBoolean("IS_POSITIVE"),
                rs.getLong("USER_ID"),
                rs.getLong("FILM_ID"),
                rs.getInt("USEFUL"));

    }

}
