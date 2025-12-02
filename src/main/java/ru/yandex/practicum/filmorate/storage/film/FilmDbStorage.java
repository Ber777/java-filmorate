package ru.yandex.practicum.filmorate.storage.film;

import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.base.BaseStorage;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import ru.yandex.practicum.filmorate.util.SqlQueries;

@Repository("filmDbStorage")
public class FilmDbStorage extends BaseStorage<Film> implements FilmStorage {

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmRowMapper rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return getMany(SqlQueries.allFilms);
    }

    @Override
    public Film getFilm(Long id) {
        Optional<Film> one = getOne(SqlQueries.byIdFilm, id);
        return one.orElseThrow(() -> new NotFoundException("Фильм id:" + id + " не найден"));
    }

    @Override
    public void checkFilmExists(Long id) {
        boolean exists = exists(SqlQueries.checkFilmExists, id);
        if (!exists) {
            throw new NotFoundException("Фильм id:" + id + " не найден");
        }
    }

    @Override
    public Film create(Film film) {

        Set<Short> genreIds = film.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());

        checkGenresExist(genreIds);

        Long id = insertAndReturnId(SqlQueries.insertFilm,
                Long.class,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId()
        );

        if (id == null) {
            return null;
        }

        film.setId(id);
        updateFilmGenresAssociation(film);
        return getFilm(id);
    }

    @Override
    public Film update(Film film) {
        super.update(SqlQueries.updateFilm,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId());

        updateFilmGenresAssociation(film);
        return getFilm(film.getId());
    }

    @Override
    public Collection<Film> getTopFilms(int count) {
        return getMany(SqlQueries.popularFilm, count);
    }

    private void updateFilmGenresAssociation(Film film) {
        final Collection<Genre> genres = film.getGenres();

        Set<Short> genreIds = genres.stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());

        update(SqlQueries.deleteFilmGenre, film.getId());

        if (genreIds.isEmpty()) {
            return;
        }

        jdbcTemplate.batchUpdate(SqlQueries.insertFilmGenre, genreIds, genreIds.size(),
                (ps, genreId) -> {
                    ps.setLong(1, film.getId());
                    ps.setInt(2, genreId);
                });
    }

    private void checkGenresExist(Set<Short> genreIds) throws NotFoundException {
        if (genreIds == null || genreIds.isEmpty()) {
            return;
        }

        String sql = "SELECT id FROM genres WHERE id IN (" +
                genreIds.stream()
                        .map(id -> "?")
                        .collect(Collectors.joining(",")) +
                ")";

        Set<Short> existingIds = new HashSet<>(
                jdbcTemplate.queryForList(sql, Short.class, genreIds.toArray())
        );

        Set<Short> invalidIds = genreIds.stream()
                .filter(id -> !existingIds.contains(id))
                .collect(Collectors.toSet());

        if (!invalidIds.isEmpty()) {
            throw new NotFoundException("Не найдены жанры с ID: " + invalidIds);
        }
    }
}