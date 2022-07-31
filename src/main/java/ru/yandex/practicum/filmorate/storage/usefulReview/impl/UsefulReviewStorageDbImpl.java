package ru.yandex.practicum.filmorate.storage.usefulReview.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.usefulReview.UsefulReviewStorage;

@Component
public class UsefulReviewStorageDbImpl implements UsefulReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UsefulReviewStorageDbImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean addLikeReview(Long reviewId, Long userId) {
                String sqlQuery = "insert into USEFUL_REVIEW (REVIEW_ID, USER_ID, REVIEW_LIKE) " +
                        "values (?, ?, ?) /*on conflict do nothing*/";

                return jdbcTemplate.update(sqlQuery, reviewId, userId, true) > 0;

    }

    @Override
    public boolean addDislikeReview(Long reviewId, Long userId) {
        String sqlQuery = "insert into USEFUL_REVIEW (REVIEW_ID, USER_ID, REVIEW_LIKE) " +
                "values (?, ?, ?) /*on conflict do nothing*/ ";

        return jdbcTemplate.update(sqlQuery, reviewId, userId, false) > 0;
    }

    @Override
    public boolean deleteLikeReview(Long reviewId, Long userId) {
        String sqlQuery = "delete from USEFUL_REVIEW where REVIEW_ID = ? and USER_ID = ? and REVIEW_LIKE = ?";
        return jdbcTemplate.update(sqlQuery, reviewId, userId, true) > 0;
    }

    @Override
    public boolean deleteDislikeReview(Long reviewId, Long userId) {
        String sqlQuery = "delete from USEFUL_REVIEW where REVIEW_ID = ? and USER_ID = ? and REVIEW_LIKE = ?";
        return jdbcTemplate.update(sqlQuery, reviewId, userId, false) > 0;
    }

}
