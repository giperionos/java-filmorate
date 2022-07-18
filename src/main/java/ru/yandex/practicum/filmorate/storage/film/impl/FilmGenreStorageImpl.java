package ru.yandex.practicum.filmorate.storage.film.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.film.FilmGenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class FilmGenreStorageImpl implements FilmGenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmGenreStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(Long filmId, Integer genreId) {
        String sqlQuery = "insert into FILM_GENRE (FILM_ID, GENRE_ID) VALUES ( ?, ? )";

        jdbcTemplate.update(sqlQuery, filmId, genreId);
    }

    @Override
    public boolean deleteByFilmId(Long id) {
        String sqlQuery = "delete from FILM_GENRE where FILM_ID = ?";
        return jdbcTemplate.update(sqlQuery, id) > 0;
    }

    @Override
    public List<FilmGenre> getByFilmId(Long id) {
        String sqlQuery = "select * from FILM_GENRE where FILM_ID = ?";
        return  jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToFilmGenre(rs)), id);
    }

    @Override
    public List<FilmGenre> getAll() {
        String sqlQuery = "select * from FILM_GENRE";
        return jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToFilmGenre(rs)));
    }

    public FilmGenre mapRowToFilmGenre(ResultSet rs) throws SQLException {
        return new FilmGenre(
                rs.getLong("FILM_ID"),
                rs.getInt("GENRE_ID")
        );
    }
}
