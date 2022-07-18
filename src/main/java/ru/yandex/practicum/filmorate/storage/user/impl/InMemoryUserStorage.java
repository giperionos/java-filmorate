package ru.yandex.practicum.filmorate.storage.user.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryEntityStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Component
public class InMemoryUserStorage extends InMemoryEntityStorage<User> implements UserStorage {
}
