package ru.yandex.practicum.filmorate.service.Film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    public Film addLike(Long id, Long userId) {
        Film film = filmStorage.getFilm(id);
        userStorage.getUser(userId);
        film.addLike(userId);
        return film;
    }

    public Film removeLike(Long id, Long userId) {
        Film film = filmStorage.getFilm(id);
        userStorage.getUser(userId);
        film.removeLike(userId);
        return film;
    }

    public Collection<Film> getTopFilms(int count) {
        return filmStorage.getTopFilms(count);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilm(Long id) {
        return filmStorage.getFilm(id);
    }

    public Film create(Film film) {
        film.clearLikes();
        return filmStorage.create(film);
    }

    public Film update(Film film) {

        Film updatedFilm = filmStorage.getFilm(film.getId());
        if (film.getName() != null)
            updatedFilm.setName(film.getName());

        if (film.getDuration() != null)
            updatedFilm.setDuration(film.getDuration());

        if (film.getDescription() != null)
            updatedFilm.setDescription(film.getDescription());

        if (film.getReleaseDate() != null)
            updatedFilm.setReleaseDate(film.getReleaseDate());

        return filmStorage.update(updatedFilm);
    }
}
