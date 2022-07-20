package ru.yandex.practicum.filmorate.storage.usefulReview.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.storage.usefulReview.UsefulReviewStorage;

import java.lang.reflect.InvocationTargetException;

@Component
public class UsefulReviewDbStorage implements UsefulReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UsefulReviewDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean addLikeReview(Long id, Long userId) {
                String sqlQuery = "insert into USEFUL_REVIEW (REVIEW_ID, USER_ID, REVIEW_LIKE) " +
                        "values (?, ?, ?) /*on conflict do nothing*/";

                return jdbcTemplate.update(sqlQuery, id, userId, true) > 0;

    }

    @Override
    public boolean addDislikeReview(Long id, Long userId) {
        String sqlQuery = "insert into USEFUL_REVIEW (REVIEW_ID, USER_ID, REVIEW_LIKE) " +
                "values (?, ?, ?) /*on conflict do nothing*/ ";

        return jdbcTemplate.update(sqlQuery, id, userId, false) > 0;
    }

    @Override
    public boolean deleteLikeReview(Long id, Long userId) {
        String sqlQuery = "delete from USEFUL_REVIEW where REVIEW_ID = ? and USER_ID = ? and REVIEW_LIKE = ?";
        return jdbcTemplate.update(sqlQuery, id, userId, true) > 0;
    }

    @Override
    public boolean deleteDislikeReview(Long id, Long userId) {
        String sqlQuery = "delete from USEFUL_REVIEW where REVIEW_ID = ? and USER_ID = ? and REVIEW_LIKE = ?";
        return jdbcTemplate.update(sqlQuery, id, userId, false) > 0;
    }

}
