package ru.yandex.practicum.filmorate.storage.review.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class ReviewStorageDbImpl implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewStorageDbImpl(JdbcTemplate jdbcTemplate) {
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

            final long reviewId = keyHolder.getKey().longValue();
            review.setReviewId(reviewId);
            return getReviewById(reviewId);


    }

    @Override
    public Review updateReview(Review review) {
        String sqlQuery = "update REVIEW set REVIEW_CONTENT = ?, IS_POSITIVE = ?, USEFUL = ? "
                + " where REVIEW_ID = ?";

        int isUpdated = jdbcTemplate.update(sqlQuery,
                review.getContent(),
                review.getIsPositive(),
                0,
                review.getReviewId()
        );

        if (isUpdated == 0) {
            throw new EntityNotFoundException(String.format("Сущность с id %d не найдена в хранилище.",
                    review.getReviewId()));
        }

        return getReviewById(review.getReviewId());
    }

    @Override
    public void deleteReview(Long reviewId) {
        String sqlQuery = "delete from REVIEW where REVIEW_ID = ?";

        boolean deleting = jdbcTemplate.update(sqlQuery, reviewId) > 0;
        if (!deleting){
            throw new EntityNotFoundException(String.format("Сущность с reviewId %d не найдена в таблице REVIEW:", reviewId));
        }
    }

    @Override
    public Review getReviewById(Long reviewId) {
        String sqlQuery = "select * from REVIEW where REVIEW_ID = ?";
        List<Review> reviewes = jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> makeReview(rs)), reviewId);
        if (reviewes.size() != 1) {
            throw new EntityNotFoundException(String.format("Сущность с %d не найдена в таблице REVIEW:", reviewId));
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
    public void addLike(Long reviewId) {
        String sqlQuery = "update REVIEW set USEFUL = USEFUL + ? "
                + " where REVIEW_ID = ?";

        int isUpdated = jdbcTemplate.update(sqlQuery,1, reviewId);
    }

    @Override
    public void addDislike(Long reviewId) {
        String sqlQuery = "update REVIEW set USEFUL = USEFUL - ? "
                + " where REVIEW_ID = ?";

        jdbcTemplate.update(sqlQuery,1, reviewId);
    }

    @Override
    public void deleteLike(Long reviewId) {
        String sqlQuery = "update REVIEW set USEFUL = USEFUL - ? "
                + " where REVIEW_ID = ?";

        jdbcTemplate.update(sqlQuery,1, reviewId);
    }

    @Override
    public void deleteDislike(Long reviewId) {
        String sqlQuery = "update REVIEW set USEFUL = USEFUL + ? "
                + " where REVIEW_ID = ?";

        jdbcTemplate.update(sqlQuery,1, reviewId);
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
