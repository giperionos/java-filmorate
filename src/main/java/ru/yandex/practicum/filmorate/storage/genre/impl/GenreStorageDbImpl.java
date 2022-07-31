package ru.yandex.practicum.filmorate.storage.genre.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GenreStorageDbImpl implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreStorageDbImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreById(Integer genreId) {
        String sqlQuery = "select * from GENRE where GENRE_ID = ?";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToGenre(rs)), genreId);
        if (genres.size() != 1) {
            throw new EntityNotFoundException(String.format("Сущность с %d не найдена в таблице GENRE:", genreId));
        }
        return genres.get(0);
    }

    @Override
    public List<Genre> getAllGenres() {
        String sqlQuery = "select * from GENRE";
        return jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToGenre(rs)));
    }

    @Override
    public void addNewGenre(Genre genre) {
        String sqlQuery = "MERGE INTO GENRE (GENRE_ID, GENRE_NAME) VALUES (?, ? );";

        jdbcTemplate.update(sqlQuery, genre.getId(), genre.getName());
    }

    public Genre mapRowToGenre(ResultSet rs) throws SQLException {
        return new Genre(
                rs.getInt("GENRE_ID"),
                rs.getString("GENRE_NAME")
        );
    }
}
