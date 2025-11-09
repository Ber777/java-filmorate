package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Create;
import ru.yandex.practicum.filmorate.model.Update;
import ru.yandex.practicum.filmorate.model.User;

import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @PostMapping
    public User create(@RequestBody @Validated(Create.class) User user) {
        log.info("Получен HTTP-запрос на создание пользователя: {}", user);
        user.setId(getNextId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Пользователь id:{} был добавлен: {}", user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody @Validated(Update.class) User user) {
        log.info("Получен HTTP-запрос на обновление пользователя: {}", user);
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }

        User copyUser = users.get(user.getId());

        if (user.getName() != null)
            copyUser.setName(user.getName());

        if (user.getEmail() != null)
            copyUser.setEmail(user.getEmail());

        if (user.getLogin() != null)
            copyUser.setLogin(user.getLogin());

        if (user.getBirthday() != null)
            copyUser.setBirthday(user.getBirthday());

        users.put(copyUser.getId(), copyUser);
        log.info("Пользователь id:{} был обновлен: {}", copyUser.getId(), copyUser);
        return copyUser;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен HTTP-запрос на получение всех пользователей");
        return users.values();
    }
}
