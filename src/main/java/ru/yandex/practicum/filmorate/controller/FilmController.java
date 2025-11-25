package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.Film.FilmService;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")  // вынесли на уровень класса
@RequiredArgsConstructor
public class FilmController {
    private static final LocalDate FIRST_FILM_RELEASE_DATE
            = LocalDate.of(1895, 12, 28);
    private final FilmService filmService;

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        validate(film);
        log.info("Получен HTTP-запрос на добавления фильма: {}", film);
        Film createdFilm = filmService.create(film);
        log.info("Успешно обработан HTTP-запрос на добавления фильма: {}", film);
        return createdFilm;

    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        validate(film);
        log.info("Получен HTTP-запрос на обновление фильма: {}", film);
        Film updatedFilm = filmService.update(film);
        log.info("Фильм id:{} был обновлен: {}", updatedFilm.getId(), updatedFilm);
        return updatedFilm;
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public Film like(@PathVariable Long id, @PathVariable Long userId) {
        Film film = filmService.addLike(id, userId);
        log.info("Пользователт id:{} лайкнул фильм id:{}", userId, id);
        return film;
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public Film unLike(@PathVariable Long id, @PathVariable Long userId) {
        Film film = filmService.removeLike(id, userId);
        log.info("Пользователь id:{} удалил лайк с фильма id:{}", userId, id);
        return film;
    }

    private void validate(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(FIRST_FILM_RELEASE_DATE))
            throw new ValidationException("Дата релиза не может быть ранее "
                    + FIRST_FILM_RELEASE_DATE.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    @GetMapping(value = "/popular")
    public Collection<Film> getTopFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Получен HTTP-запрос на получение топ фильмов.");
        if (count <= 0)
            throw new ValidationException("Укажите значение больше нуля.");
        return filmService.getTopFilms(count);
    }

    @GetMapping(value = "/{id}")
    public Film getFilm(@PathVariable Long id) {
        log.info("Получен HTTP-запрос на получение фильма по id:{}", id);
        return filmService.getFilm(id);
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Получен HTTP-запрос на получение всех фильмов.");
        return filmService.getAllFilms();
    }
}
