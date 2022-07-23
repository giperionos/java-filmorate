package ru.yandex.practicum.filmorate.storage.genre.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreStorageDbImplTest {

    private final GenreStorageDbImpl genreStorage;

    @Test
    void testFindGenreById() {
        Genre dbGenre1 = genreStorage.getGenreById(1);
        assertEquals(1, dbGenre1.getId(), "id не совпадает.");
        assertEquals("Комедия", dbGenre1.getName(), "Name не совпадает.");
    }

    @Test
    void testGetAllGenres() {

        List<Genre> dbGenres = genreStorage.getAllGenres();

        List<Genre> expectedGenres = List.of(
                new Genre(1, "Комедия"),
                new Genre(2, "Драма"),
                new Genre(3, "Мультфильм"),
                new Genre(4, "Триллер"),
                new Genre(5, "Документальный"),
                new Genre(6, "Боевик")
        );

        assertTrue(dbGenres.containsAll(expectedGenres), "Список полученный из БД не совпадает с ожидаемым.");
    }
}