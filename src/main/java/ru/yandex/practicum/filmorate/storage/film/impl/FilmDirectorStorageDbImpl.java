package ru.yandex.practicum.filmorate.storage.film.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmDirector;
import ru.yandex.practicum.filmorate.storage.film.FilmDirectorStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class FilmDirectorStorageDbImpl implements FilmDirectorStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDirectorStorageDbImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addNewFilmDirectorLink(Long filmId, Integer directorId) {
        String sqlQuery = "insert into FILM_DIRECTOR (FILM_ID, DIRECTOR_ID) VALUES ( ?, ? )";

        jdbcTemplate.update(sqlQuery, filmId, directorId);
    }

    @Override
    public List<FilmDirector> getFilmDirectorLinksByFilmId(Long filmId) {
        String sqlQuery = "select * from FILM_DIRECTOR where FILM_ID = ?";
        return  jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToFilmDirector(rs)), filmId);
    }

    @Override
    public boolean deleteFilmDirectorLinksByFilmId(Long filmId) {
        String sqlQuery = "delete from FILM_DIRECTOR where FILM_ID = ?";
        return jdbcTemplate.update(sqlQuery, filmId) > 0;
    }

    private FilmDirector mapRowToFilmDirector(ResultSet rs) throws SQLException {
        return new FilmDirector(
                rs.getLong("FILM_ID"),
                rs.getInt("DIRECTOR_ID")
        );
    }
}
