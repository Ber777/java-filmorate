package ru.yandex.practicum.filmorate.exception;

public class FriendExistsException extends RuntimeException {
    public FriendExistsException(Long id1, Long id2) {
        super("Пользователь id:" + id1 + " уже в друзьях у пользователя id:" + id2);
    }
}
