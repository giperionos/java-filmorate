package ru.yandex.practicum.filmorate.storage.rating.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.MPARating;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RatingStorageImplTest {

    private final RatingStorageImpl ratingStorage;

    @Test
    public void testFindRatingById() {

       MPARating mpa = ratingStorage.getById(1);
       assertEquals(1, mpa.getId(), "id не совпадает.");
       assertEquals("G", mpa.getName(), "Name не совпадает.");
       assertEquals("У фильма нет возрастных ограничений", mpa.getDescription(), "Description не совпадает.");
    }

    @Test
    public void testGetAllRatings() {
        List<MPARating> dbRatings = ratingStorage.getAll();

        assertEquals(5, dbRatings.size(), "Количество записей не совпадает с ожидаемым.");

        MPARating mpaG = new MPARating(1, "G", "У фильма нет возрастных ограничений" );
        MPARating mpaPG = new MPARating(2, "PG", "Детям рекомендуется смотреть фильм с родителями"  );
        MPARating mpaPG13 = new MPARating(3, "PG-13", "Детям до 13 лет просмотр не желателен" );
        MPARating mpaR = new MPARating(4, "R", "Лицам до 17 лет просматривать фильм можно только в присутствии взрослого"  );
        MPARating mpaNC17 = new MPARating(5, "NC-17", "Лицам до 18 лет просмотр запрещён" );

        List<MPARating> expectedRatings = List.of(mpaG, mpaPG, mpaPG13, mpaR, mpaNC17);

        assertTrue(dbRatings.containsAll(expectedRatings), "Список полученный из БД не совпадает с ожидаемым.");
    }

}