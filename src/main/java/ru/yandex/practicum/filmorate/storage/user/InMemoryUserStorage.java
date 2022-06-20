package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryEntityStorage;

@Component
public class InMemoryUserStorage extends InMemoryEntityStorage<User> implements UserStorage {
}
