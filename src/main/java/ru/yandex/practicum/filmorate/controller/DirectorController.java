package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.director.DirectorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/directors")
@Slf4j
public class DirectorController {

    private final DirectorService directorService;

    @Autowired
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping
    public List<Director> getAllDirectors() {
        return directorService.getAllDirectors();
    }

    @GetMapping("/{directorId}")
    public Director getDirectorById(@PathVariable Integer directorId) {
        return directorService.getDirectorById(directorId);
    }

    @PostMapping
    public Director addNewDirector(@Valid @RequestBody Director director) {
        return directorService.addNewDirector(director);
    }

    @PutMapping
    public Director updateDirector(@Valid @RequestBody Director director) {
        return  directorService.updateDirector(director);
    }

    @DeleteMapping("/{directorId}")
    public void removeDirectorById(@PathVariable Integer directorId) {
        directorService.removeDirectorById(directorId);
    }
}
