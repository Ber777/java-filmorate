package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

class FilmControllerTest {
    FilmController filmController = new FilmController();

    @Test
    void getFilm() {
        LocalDate now = LocalDate.now();
        Film film1 = new Film(1L, "name1", "description1", now, 10);
        filmController.create(film1);
        Film errFilm = new Film(1L, "name1", "description1",
                LocalDate.of(1894, 12, 28), 0);

        assertThrows(ValidationException.class, () -> filmController.create(errFilm));
    }

}