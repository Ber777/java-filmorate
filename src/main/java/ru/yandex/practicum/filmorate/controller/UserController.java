package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.dto.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.UserResponseDto;
import ru.yandex.practicum.filmorate.dto.UserUpdateDto;
import ru.yandex.practicum.filmorate.model.Create;
import ru.yandex.practicum.filmorate.model.Update;
import ru.yandex.practicum.filmorate.service.User.UserService;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import lombok.extern.slf4j.Slf4j;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserResponseDto create(@RequestBody @Validated(Create.class) UserCreateDto user) {
        log.info("Получен HTTP-запрос на создание пользователя: {}", user);
        UserResponseDto createdUser = userService.create(user);
        log.info("Пользователь id:{} был добавлен: {}", createdUser.getId(), createdUser);
        return createdUser;
    }

    @PutMapping
    public UserResponseDto update(@RequestBody @Validated(Update.class) UserUpdateDto user) {
        log.info("Получен HTTP-запрос на обновление пользователя: {}", user);
        UserResponseDto updatedUser = userService.update(user);
        log.info("Пользователь id:{} был обновлен: {}", updatedUser.getId(), updatedUser);
        return updatedUser;
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
        log.info("Пользователь id:{} добавил в друзья пользователя id:{}", id, friendId);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.removeFriend(id, friendId);
        log.info("Пользователь id:{} удалил из друзей пользователя id:{}", id, friendId);
    }

    @GetMapping(value = "/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public Collection<UserResponseDto> getFriends(@PathVariable Long id) {
        log.info("Получен HTTP-запрос на получение списка друзей пользователя id:{}", id);
        return userService.getFriends(id);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<UserResponseDto> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Получен HTTP-запрос на получение списка друзей пользователя id:{}, " +
                "общих с другим пользователем id:{}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping(value = "/{id}")
    public UserResponseDto getUser(@PathVariable Long id) {
        log.info("Получен HTTP-запрос на получение пользователя по id:{}", id);
        return userService.getUser(id);
    }

    @GetMapping
    public Collection<UserResponseDto> getAllUsers() {
        log.info("Получен HTTP-запрос на получение всех пользователей");
        return userService.getAllUsers();
    }
}
