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
        return directorStorage.getAll();
    }

    public Director getDirectorById(Integer id) {
        return directorStorage.getById(id);
    }

    public Director addNewDirector(Director director) {
        return directorStorage.add(director);
    }

    public Director updateDirector(Director director) {
        return directorStorage.update(director);
    }

    public void removeDirectorById(Integer id) {
        directorStorage.deleteById(id);
    }
}
