package ru.yandex.practicum.filmorate.storage.film.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;

import java.sql.*;
import java.sql.Date;
import java.util.*;

@Component
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private RatingStorage ratingStorage;
    private FilmGenreStorage filmGenreStorage;
    private GenreStorage genreStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, RatingStorage ratingStorage, FilmGenreStorage filmGenreStorage, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.ratingStorage = ratingStorage;
        this.filmGenreStorage = filmGenreStorage;
        this.genreStorage = genreStorage;
    }

    @Override
    public Film add(Film entity) {
        String sqlQuery = "insert into FILMS (FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, DURATION, RATING_MPA_ID) " +
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

        //поскольку от фронта рейтинг приходит как объект лишь с одним полем id,
        //а в бд для рейтинга несколько полей, то их нужно заполнить до конца.
        entity.setMpa(ratingStorage.getById(entity.getMpa().getId()));

        //поскольку от фронта жанр приходит как объект лишь с одним полем id,
        //а в бд для жанров несколько полей, то их нужно заполнить до конца.
        //а также добавить эти жанры в таблицу FILM_GENRE
        if (entity.getGenres() != null) {
            for (Genre genre : entity.getGenres()) {
                filmGenreStorage.add(entity.getId(), genre.getId());
                genre.setName(genreStorage.getById(genre.getId()).getName());
            }
        }

        return entity;
    }

    @Override
    public Film update(Film entity) {
        String sqlQuery = "update FILMS set FILM_NAME = ?, FILM_DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATING_MPA_ID = ?"
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

        entity.setMpa(ratingStorage.getById(entity.getMpa().getId()));

        //также нужно обновить жанры
        //сначала нужно удалить все имеющиеся
        filmGenreStorage.deleteByFilmId(entity.getId());

        //затем добавить жанры если есть
        if (entity.getGenres() != null) {


            for (Genre genre : entity.getGenres()) {
                filmGenreStorage.add(entity.getId(), genre.getId());
                genre.setName(genreStorage.getById(genre.getId()).getName());
            }

            //сейчас в Film жанры не отсортированные

            //коллекция сортированных жанров
            Set<Genre> filmGenres = new TreeSet<>(new Comparator<Genre>() {
                @Override
                public int compare(Genre o1, Genre o2) {
                    return -1 * o2.getId().compareTo(o1.getId());
                }
            });

            //наполнить ее
            filmGenres.addAll(entity.getGenres());

            //заменить в текущем экземпляре Film
            entity.setGenres(filmGenres);
        }

        return entity;
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "select * from FILMS";
        return jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToFilm(rs)));
    }

    @Override
    public Film getById(Long id) {
        String sqlQuery = "select * from FILMS where FILM_ID = ?";
        List<Film> users = jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToFilm(rs)), id);

        if (users.size() != 1) {
            throw new EntityNotFoundException(String.format("Сущность с %d не найдена в таблице USERS:", id));
        }

        return users.get(0);
    }

    public boolean deleteAll() {
        String sqlQuery = "delete from FILMS cascade;";

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
                "    T.RATING_MPA_ID\n" +
                "from (\n" +
                "    select\n" +
                "        F.* ,\n" +
                "        (select count(1) from LIKES L where L.FILM_ID = F.FILM_ID) as priority\n" +
                "    from FILMS F\n" +
                ") T\n" +
                "order by T.priority desc\n" +
                "LIMIT ?;";

        return jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> mapRowToFilm(rs)), count);
    }

    //likes
    public Film mapRowToFilm(ResultSet rs) throws SQLException {

        Set<Genre> filmGenres = new TreeSet<>(new Comparator<Genre>() {
            @Override
            public int compare(Genre o1, Genre o2) {
                return -1 * o2.getId().compareTo(o1.getId());
            }
        });

        for (FilmGenre filmGenre: filmGenreStorage.getByFilmId(rs.getLong("FILM_ID"))){
            filmGenres.add(genreStorage.getById(filmGenre.getGenreId()));
        }

        return new Film(
                rs.getLong("FILM_ID"),
                rs.getString("FILM_NAME"),
                rs.getString("FILM_DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getInt("DURATION"),
                ratingStorage.getById(rs.getInt("RATING_MPA_ID")),
                filmGenres
        );
    }
}
