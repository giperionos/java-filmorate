package ru.yandex.practicum.filmorate.storage.film.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UnknownFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.*;
import java.sql.Date;
import java.util.*;

@Component
@Qualifier("filmDbStorage")
public class FilmStorageDbImpl implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public FilmStorageDbImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addNewFilm(Film entity) {
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
    public Film updateFilm(Film entity) {
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
            throw new UnknownFilmException(String.format("?????????? ?? %d ???? ???????????? ?? ?????????????? FILM.", entity.getId()));
        }

        return entity;
    }

    @Override
    public List<Film> getAllFilms() {
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
    public Film getFilmById(Long filmId) {
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

        List<Film> films = jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToFilm(rs)), filmId);

        if (films.size() != 1) {
            throw new UnknownFilmException(String.format("?????????? ?? %d ???? ???????????? ?? ?????????????? FILM.", filmId));
        }

        return films.get(0);
    }

    public boolean deleteAll() {
        String sqlQuery = "delete from FILM;";
        return jdbcTemplate.update(sqlQuery) > 0;
    }

    @Override
    public List<Film> getMostPopularFilmsList(Long count, Optional<Integer> genreId, Optional<Integer> year) {
        MapSqlParameterSource sqlQueryParams = new MapSqlParameterSource();
        sqlQueryParams.addValue("limit", count);
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
                "    left join RATING_MPA RM on RM.RATING_ID = F.RATING_MPA_ID\n";
                if (genreId.isPresent()) {
                    sqlQuery += "left join FILM_GENRE FG on F.FILM_ID = FG.FILM_ID " +
                                "where FG.GENRE_ID = :genreId ";
                    sqlQueryParams.addValue("genreId", genreId.get());
                }
                if (year.isPresent()) {
                    sqlQuery += (genreId.isPresent() ? "and " : "where ");
                    sqlQuery += "extract(year from RELEASE_DATE) = :year\n";
                    sqlQueryParams.addValue("year", year.get());
                }
                sqlQuery += ") T\n " +
                            "order by T.priority desc\n" +
                            "LIMIT :limit";

        return namedParameterJdbcTemplate.query(sqlQuery, sqlQueryParams, (rs, rowNum) -> mapRowToFilm(rs));
    }

    public void deleteFilmById(Long filmId) {
        String sqlQuery = "delete from FILM where FILM_ID = ?";
        if (jdbcTemplate.update(sqlQuery, filmId) == 0) {
            throw new UnknownFilmException(String.format("???????????????? ?? id=%d ???? ?????????????? ?? ?????????????? FILM:", filmId));
        }
    }

    @Override
    public Collection<Film> getRecommendations(Long userId) {
        final String sqlQuery = "select\n" +
                "    FILM_ID\n" +
                "from \"LIKE\" as L\n" +
                "    inner join (\n" +
                "    select top 1\n" +
                "        USER_ID,\n" +
                "        COUNT(FILM_ID) as LIKE_COUNT\n" +
                "    from \"LIKE\"\n" +
                "    where USER_ID <> ? and\n" +
                "          FILM_ID in (select\n" +
                "                        FILM_ID\n" +
                "                      from \"LIKE\"\n" +
                "                      where USER_ID = ?)\n" +
                "    group by USER_ID\n" +
                "    order by LIKE_COUNT desc) as M\n" +
                "on M.USER_ID = L.USER_ID\n" +
                "where FILM_ID not in (\n" +
                "    select\n" +
                "        FILM_ID\n" +
                "    from \"LIKE\"\n" +
                "    where USER_ID = ?\n" +
                "    )";

        return jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> getFilmById(rs.getLong("FILM_ID"))), userId, userId, userId);
    }

    //likes
    public Film mapRowToFilm(ResultSet rs) throws SQLException {

        return new Film(
                rs.getLong("FILM_ID"),
                rs.getString("FILM_NAME"),
                rs.getString("FILM_DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getInt("DURATION"),
                new MpaRating(rs.getInt("RATING_ID"), rs.getString("RATING_NAME"), rs.getString("RATING_DESCRIPTION")),
                null,
                null
        );
    }

    @Override
    public List<Film> getFilmsForDirectorSortedByYears(Integer directorId) {
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
                "LEFT JOIN FILM_DIRECTOR FD on F.FILM_ID = FD.FILM_ID\n" +
                "where fd.DIRECTOR_ID = ?\n" +
                "order by F.RELEASE_DATE ;";

        return jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToFilm(rs)), directorId);
    }

    @Override
    public List<Film> getFilmsForDirectorSortedByLikes(Integer directorId) {
        String sqlQuery = "select\n" +
                "    F.FILM_ID,\n" +
                "    F.FILM_NAME,\n" +
                "    F.FILM_DESCRIPTION,\n" +
                "    F.RELEASE_DATE,\n" +
                "    F.DURATION,\n" +
                "    F.RATING_MPA_ID,\n" +
                "    RM.RATING_ID,\n" +
                "    RM.RATING_NAME,\n" +
                "    RM.RATING_DESCRIPTION,\n" +
                "    (select count(1) from \"LIKE\" L where L.FILM_ID = F.FILM_ID) as priority\n" +
                "from FILM F\n" +
                "LEFT JOIN RATING_MPA RM on RM.RATING_ID = F.RATING_MPA_ID\n" +
                "LEFT JOIN FILM_DIRECTOR FD on F.FILM_ID = FD.FILM_ID\n" +
                "where fd.DIRECTOR_ID = ?\n" +
                "order by priority desc;";

        return jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToFilm(rs)), directorId);
    }

    @Override
    public List<Film> getFilmsForDirectorSortedByYearsAndLikes(Integer directorId) {
        String sqlQuery = "select\n" +
                "    F.FILM_ID,\n" +
                "    F.FILM_NAME,\n" +
                "    F.FILM_DESCRIPTION,\n" +
                "    F.RELEASE_DATE,\n" +
                "    F.DURATION,\n" +
                "    F.RATING_MPA_ID,\n" +
                "    RM.RATING_ID,\n" +
                "    RM.RATING_NAME,\n" +
                "    RM.RATING_DESCRIPTION,\n" +
                "    (select count(1) from \"LIKE\" L where L.FILM_ID = F.FILM_ID) as priority\n" +
                "from FILM F\n" +
                "LEFT JOIN RATING_MPA RM on RM.RATING_ID = F.RATING_MPA_ID\n" +
                "LEFT JOIN FILM_DIRECTOR FD on F.FILM_ID = FD.FILM_ID\n" +
                "where fd.DIRECTOR_ID = ?\n" +
                "order by F.RELEASE_DATE, priority desc;";

        return jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToFilm(rs)), directorId);
    }

    public List<Film> getFilmsByQuery(String query, String queryParam) {
        final String sqlQuery =
                "SELECT " +
                "    FR.*, " +
                "    RM.* " +
                "FROM " +
                "    (SELECT " +
                "        F.FILM_ID, " +
                "        GROUP_CONCAT(D.DIRECTOR_NAME SEPARATOR '|') AS director, " +
                "        FILM_NAME title " +
                "    FROM FILM F " +
                "    LEFT JOIN FILM_DIRECTOR FD on F.FILM_ID = FD.FILM_ID " +
                "    LEFT JOIN DIRECTOR D on FD.DIRECTOR_ID = D.DIRECTOR_ID " +
                "    GROUP BY F.FILM_ID) F " +
                "LEFT JOIN FILM FR ON FR.FILM_ID = F.FILM_ID " +
                "LEFT JOIN RATING_MPA RM on FR.RATING_MPA_ID = RM.RATING_ID " +
                "LEFT JOIN ( " +
                "    SELECT " +
                "        FILM_ID, " +
                "        COUNT(USER_ID) LIKES " +
                "    FROM \"LIKE\" " +
                "    GROUP BY FILM_ID " +
                "    ) L ON F.FILM_ID = L.FILM_ID " +
                "WHERE CONCAT_WS('|', " + queryParam + " , '|') ILIKE CONCAT('%',?,'%') " +
                "ORDER BY LIKES DESC";

        return jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToFilm(rs)), query);
    }

    public List<Film> getCommonFilmsByRating(Long userId, Long friendId){
        String sqlQuery = "SELECT " +
                "f.FILM_ID, " +
                "f.FILM_NAME, " +
                "f.FILM_DESCRIPTION, " +
                "f.RELEASE_DATE, " +
                "f.DURATION, " +
                "mp.RATING_ID, " +
                "mp.RATING_NAME, " +
                "mp.RATING_DESCRIPTION " +
                "from FILM AS f " +
                "LEFT JOIN RATING_MPA as mp on f.RATING_MPA_ID = mp.RATING_ID " +
                "LEFT JOIN \"LIKE\" L on f.FILM_ID = L.FILM_ID" +
                " where f.FILM_ID in " +
                "(select FILM_ID from \"LIKE\" " +
                "where USER_ID = ? and FILM_ID in " +
                "(select FILM_ID from \"LIKE\" where USER_ID = ?)) " +
                "group by L.FILM_ID order by count(L.USER_ID) desc";
        return jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToFilm(rs)), friendId, userId);
    }
}
