package ru.yandex.practicum.filmorate.exception;

public class LikeNotFoundException extends RuntimeException {
    public LikeNotFoundException(Long id, Long userId) {
        super("Фильм id:" + id + " не был оценен пользователем id:" + userId);
    }
}
