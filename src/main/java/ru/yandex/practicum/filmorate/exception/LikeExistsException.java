package ru.yandex.practicum.filmorate.exception;

public class LikeExistsException extends RuntimeException {
    public LikeExistsException(Long id, Long userId) {
        super("Фильм id:" + id + " уже был оценен пользователем id:" + userId);
    }
}
