package ru.yandex.practicum.filmorate.storage.director.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class DirectorStorageImpl implements DirectorStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DirectorStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Director> getAll() {

        String sqlQuery = "select * from DIRECTOR order by DIRECTOR_ID asc;";

        return jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToDirector(rs)));
    }

    @Override
    public Director getById(Integer id) {
        String sqlQuery = "select * from DIRECTOR where DIRECTOR_ID = ?;";

        List<Director> directors = jdbcTemplate.query(sqlQuery, (((rs, rowNum) -> mapRowToDirector(rs))), id);

        if (directors.size() != 1) {
            throw new EntityNotFoundException(String.format("Сущность с %d не найдена в таблице DIRECTOR.", id));
        }

        return directors.get(0);
    }

    @Override
    public Director add(Director director) {
        String sqlQuery = "insert into DIRECTOR (DIRECTOR_NAME) values (?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"DIRECTOR_ID"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);

        director.setId(keyHolder.getKey().intValue());

        return director;
    }

    @Override
    public Director update(Director director) {

        String sqlQuery = "update DIRECTOR set DIRECTOR_NAME = ? where DIRECTOR_ID = ?;";

        int isUpdated = jdbcTemplate.update(sqlQuery,
                director.getName(),
                director.getId()
        );

        if (isUpdated == 0) {
            throw new EntityNotFoundException(String.format("Сущность с %d не найдена в таблице DIRECTOR.", director.getId()));
        }

        return director;
    }

    @Override
    public void deleteById(Integer id) {
        String sqlQuery = "delete from DIRECTOR where DIRECTOR_ID = ?";

        if (jdbcTemplate.update(sqlQuery, id) == 0) {
            throw new EntityNotFoundException(String.format("Сущность DIRECTOR с id=%d не найдена в таблице DIRECTOR.", id));
        }
    }


    private Director mapRowToDirector(ResultSet rs) throws SQLException {
        return new Director(
                rs.getInt("DIRECTOR_ID"),
                rs.getString("DIRECTOR_NAME")
        );
    }
}
