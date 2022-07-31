package ru.yandex.practicum.filmorate.service.director;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.util.List;

@Service
public class DirectorService {

    private final DirectorStorage directorStorage;

    @Autowired
    public DirectorService(DirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
    }

    public List<Director> getAllDirectors() {
        return directorStorage.getAllDirectors();
    }

    public Director getDirectorById(Integer directorId) {
        return directorStorage.getDirectorById(directorId);
    }

    public Director addNewDirector(Director director) {
        return directorStorage.addNewDirector(director);
    }

    public Director updateDirector(Director director) {
        return directorStorage.updateDirector(director);
    }

    public void removeDirectorById(Integer directorId) {
        directorStorage.deleteDirectorById(directorId);
    }
}
