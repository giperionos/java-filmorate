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
public class GenreStorageImpl implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getById(Integer id) {
        String sqlQuery = "select * from GENRE where GENRE_ID = ?";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToGenre(rs)), id);
        if (genres.size() != 1) {
            throw new EntityNotFoundException(String.format("Сущность с %d не найдена в таблице GENRE:", id));
        }
        return genres.get(0);
    }

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "select * from GENRE";
        return jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToGenre(rs)));
    }

    @Override
    public void add(Genre genre) {
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
