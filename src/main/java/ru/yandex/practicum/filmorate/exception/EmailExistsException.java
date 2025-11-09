package ru.yandex.practicum.filmorate.exception;

public class EmailExistsException extends RuntimeException {
    public EmailExistsException(String email) {
        super("Пользователь с почтой: " + email + " уже зарегистрирован в системе");
    }
}
