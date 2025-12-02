package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.base.BaseStorage;

import java.util.Collection;
import java.util.Optional;
import ru.yandex.practicum.filmorate.util.SqlQueries;

@Repository
public class GenreDbStorage extends BaseStorage<Genre> implements GenreStorage {

    public GenreDbStorage(JdbcTemplate jdbcTemplate, GenreRowMapper rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    @Override
    public Collection<Genre> getAllGenres() {
        return getMany(SqlQueries.allGenre);
    }

    @Override
    public Genre getGenre(Short id) {
        Optional<Genre> one = getOne(SqlQueries.byIdGenre, id);
        if (one.isEmpty()) {
            throw new NotFoundException("Жанр id:" + id + " не найден");
        }
        return one.get();
    }
}
