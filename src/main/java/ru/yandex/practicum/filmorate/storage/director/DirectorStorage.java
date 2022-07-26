package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorStorage {

    List<Director> getAllDirectors();

    Director getDirectorById(Integer directorId);

    Director addNewDirector(Director director);

    Director updateDirector(Director director);

    void deleteDirectorById(Integer directorId);
}
