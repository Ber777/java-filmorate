package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRowMapper;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@Import({FilmDbStorage.class, GenreDbStorage.class, FilmRowMapper.class, GenreRowMapper.class, MpaRowMapper.class})
public class FilmDbStorageTests {

    @Autowired
    private FilmDbStorage storage;
    private static final Film film1 = new Film("Film1", "Description1", 120,
            LocalDate.of(1998, 11, 1), new Mpa((short) 1, "G"));
    private static final Film film2 = new Film("Film2", "Description2", 140,
            LocalDate.of(2010, 1, 10), new Mpa((short) 1,"R"));

    @Test
    void create() {
        storage.create(film1);
        assertThatThrownBy(() -> storage.getFilm(100000L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Фильм id:100000 не найден");
    }

    @Test
    void update() {
        Film created = storage.create(film1);
        created.setName("new_film");
        created.setDescription("new_desc");
        created.setDuration(20);
        created.setReleaseDate(LocalDate.of(2020, 2, 2));
        created.setMpa(new Mpa((short) 2, "PG"));
        created.setGenres(List.of(
                new Genre((short) 2, "Боевик"),
                new Genre((short) 3, "Комедия")));

        Film updated = storage.update(created);
        Film founded = storage.getFilm(created.getId());
        System.out.println("Найденные жанры = " + founded.getGenres());

        assertThat(updated).isNotNull();
        assertThat(founded).isNotNull();
        assertThat(founded.getName()).isEqualTo("new_film");
        assertThat(founded.getDescription()).isEqualTo("new_desc");
        assertThat(founded.getReleaseDate()).isEqualTo(created.getReleaseDate());
        assertThat(founded.getMpa()).extracting(Mpa::getId).isEqualTo((short) 2);
        assertThat(founded.getGenres()).extracting(Genre::getId).contains((short) 2);
        assertThat(founded.getGenres()).extracting(Genre::getId).contains((short) 3);
    }

    @Test
    void getNotExistFilm() {
        Long nonExistentId = 100000L;
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> storage.getFilm(nonExistentId)
        );
        assertThat(exception.getMessage()).contains("Фильм id:100000 не найден");
    }

    @Test
    void getAllFilms() {
        storage.create(film1);
        storage.create(film2);

        Collection<Film> films = storage.getAllFilms();
        assertThat(films).hasSize(2);
    }
}
