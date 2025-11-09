package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private static final LocalDate FIRST_FILM_RELEASE_DATE
            = LocalDate.of(1895, 12, 28);

    private final Map<Long, Film> films = new HashMap<>();

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        validate(film);
        log.info("Получен HTTP-запрос на добавления фильма: {}", film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Успешно обработан HTTP-запрос на добавления фильма: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        log.info("Получен HTTP-запрос на обновление фильма: {}", film);
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
        }

        Film copyFilm = films.get(film.getId());

        if (film.getName() != null)
            copyFilm.setName(film.getName());

        if (film.getDuration() != null)
            copyFilm.setDuration(film.getDuration());

        if (film.getDescription() != null)
            copyFilm.setDescription(film.getDescription());

        if (film.getReleaseDate() != null)
            copyFilm.setReleaseDate(film.getReleaseDate());

        // Согласен, складировать необходимо copyFilm, спасибо:
        //films.put(film.getId(), film);
        films.put(copyFilm.getId(), copyFilm);
        log.info("Фильм id:{} был обновлен: {}", copyFilm.getId(), copyFilm);
        return copyFilm;
    }

    // По комментарию о не полной валидации:
    // Остальная валидация присутсвует в классах Film, User в качестве аннотаций
    private void validate(Film film) throws ValidationException {
        validateReleaseDate(film.getReleaseDate());
    }

    private void validateReleaseDate(@NotNull LocalDate date) {
        if (date.isBefore(FIRST_FILM_RELEASE_DATE))
            throw new ValidationException("Дата релиза не может быть ранее "
                    + FIRST_FILM_RELEASE_DATE.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получен HTTP-запрос на получение всех фильмов");
        return films.values();
    }
}
