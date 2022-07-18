package ru.yandex.practicum.filmorate.storage.film.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.*;
import java.sql.Date;
import java.util.*;

@Component
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film add(Film entity) {
        String sqlQuery = "insert into FILM (FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, DURATION, RATING_MPA_ID) " +
                "VALUES ( ?, ?, ?, ?, ? )";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, entity.getName());
            stmt.setString(2, entity.getDescription());
            stmt.setDate(3, Date.valueOf(entity.getReleaseDate().toString()));
            stmt.setInt(4, entity.getDuration());
            stmt.setInt(5, entity.getMpa().getId());
            return stmt;
        }, keyHolder);

        entity.setId(keyHolder.getKey().longValue());
        return entity;
    }

    @Override
    public Film update(Film entity) {
        String sqlQuery = "update FILM set FILM_NAME = ?, FILM_DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATING_MPA_ID = ?"
                + " where FILM_ID = ?";

        int isUpdated = jdbcTemplate.update(sqlQuery,
                entity.getName(),
                entity.getDescription(),
                entity.getReleaseDate(),
                entity.getDuration(),
                entity.getMpa().getId(),
                entity.getId()
        );

        if (isUpdated == 0) {
            throw new EntityNotFoundException(String.format("Сущность с %d не найдена в хранилище.", entity.getId()));
        }

        return entity;
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "select\n" +
                "    F.FILM_ID,\n" +
                "    F.FILM_NAME,\n" +
                "    F.FILM_DESCRIPTION,\n" +
                "    F.RELEASE_DATE,\n" +
                "    F.DURATION,\n" +
                "    F.RATING_MPA_ID,\n" +
                "    RM.RATING_ID,\n" +
                "    RM.RATING_NAME,\n" +
                "    RM.RATING_DESCRIPTION\n" +
                "from FILM F\n" +
                "LEFT JOIN RATING_MPA RM on RM.RATING_ID = F.RATING_MPA_ID\n" +
                "order by F.FILM_ID asc;";

        return jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToFilm(rs)));
    }

    @Override
    public Film getById(Long id) {
        String sqlQuery = "select\n" +
                "    F.FILM_ID,\n" +
                "    F.FILM_NAME,\n" +
                "    F.FILM_DESCRIPTION,\n" +
                "    F.RELEASE_DATE,\n" +
                "    F.DURATION,\n" +
                "    F.RATING_MPA_ID,\n" +
                "    RM.RATING_ID,\n" +
                "    RM.RATING_NAME,\n" +
                "    RM.RATING_DESCRIPTION\n" +
                "from FILM F\n" +
                "LEFT JOIN RATING_MPA RM on RM.RATING_ID = F.RATING_MPA_ID\n" +
                "WHERE F.FILM_ID = ?;";


        List<Film> films = jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToFilm(rs)), id);

        if (films.size() != 1) {
            throw new EntityNotFoundException(String.format("Сущность с %d не найдена в таблице FILM:", id));
        }

        return films.get(0);
    }

    public boolean deleteAll() {
        String sqlQuery = "delete from FILM;";

        return jdbcTemplate.update(sqlQuery) > 0;
    }

    @Override
    public List<Film> getMostPopularList(Long count) {
        String sqlQuery = "select\n" +
                "    T.FILM_ID,\n" +
                "    T.FILM_NAME,\n" +
                "    T.FILM_DESCRIPTION,\n" +
                "    T.RELEASE_DATE,\n" +
                "    T.DURATION,\n" +
                "    T.RATING_MPA_ID,\n" +
                "    T.RATING_ID,\n" +
                "    T.RATING_NAME,\n" +
                "    T.RATING_DESCRIPTION\n" +
                "from (\n" +
                "    select\n" +
                    "    F.FILM_ID,\n" +
                    "    F.FILM_NAME,\n" +
                    "    F.FILM_DESCRIPTION,\n" +
                    "    F.RELEASE_DATE,\n" +
                    "    F.DURATION,\n" +
                    "    F.RATING_MPA_ID,\n" +
                    "    RM.RATING_ID,\n" +
                    "    RM.RATING_NAME,\n" +
                    "    RM.RATING_DESCRIPTION,\n" +
                "        (select count(1) from \"LIKE\" L where L.FILM_ID = F.FILM_ID) as priority\n" +
                "    from FILM F\n" +
                "    LEFT JOIN RATING_MPA RM on RM.RATING_ID = F.RATING_MPA_ID\n" +
                ") T\n" +
                "order by T.priority desc\n" +
                "LIMIT ?;";

        return jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToFilm(rs)), count);
    }

    //likes
    public Film mapRowToFilm(ResultSet rs) throws SQLException {

        return new Film(
                rs.getLong("FILM_ID"),
                rs.getString("FILM_NAME"),
                rs.getString("FILM_DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getInt("DURATION"),
                new MPARating(rs.getInt("RATING_ID"), rs.getString("RATING_NAME"), rs.getString("RATING_DESCRIPTION")),
                null
        );
    }
}
