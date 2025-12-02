package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.base.BaseStorage;

import java.util.Collection;
import java.util.Optional;
import ru.yandex.practicum.filmorate.util.SqlQueries;

@Repository
public class MpaDbStorage extends BaseStorage<Mpa> implements MpaStorage {

    public MpaDbStorage(JdbcTemplate jdbcTemplate, MpaRowMapper rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        return getMany(SqlQueries.AllMpa);
    }

    @Override
    public Mpa getMpa(Short id) {
        Optional<Mpa> one = getOne(SqlQueries.byIdMpa, id);
        if (one.isEmpty()) {
            throw new NotFoundException("Рейтинг id:" + id + " не найден");
        }
        return one.get();
    }
}
