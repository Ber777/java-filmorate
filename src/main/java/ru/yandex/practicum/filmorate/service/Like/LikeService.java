package ru.yandex.practicum.filmorate.service.Like;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.LikeExistsException;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeStorage likeStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public void addLike(Long filmId, Long userId) {

        userStorage.checkUserExists(userId);
        filmStorage.checkFilmExists(filmId);

        if (likeStorage.existsLike(filmId, userId)) {
            throw new LikeExistsException(filmId, userId);
        }

        likeStorage.save(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        userStorage.checkUserExists(userId);
        filmStorage.checkFilmExists(filmId);

        if (!likeStorage.existsLike(filmId, userId)) {
            throw new LikeNotFoundException(filmId, userId);
        }

        likeStorage.delete(filmId, userId);
    }
}
