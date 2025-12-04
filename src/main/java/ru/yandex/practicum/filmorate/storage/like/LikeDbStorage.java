package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.base.BaseStorage;
import ru.yandex.practicum.filmorate.util.SqlQueries;

@Repository
public class LikeDbStorage extends BaseStorage<Void> implements LikeStorage {

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, (rs, rowNum) -> null);
    }

    @Override
    public void save(Long filmId, Long userId) {
        jdbcTemplate.update(SqlQueries.addLike, filmId, userId);
    }

    @Override
    public void delete(Long filmId, Long userId) {
        jdbcTemplate.update(SqlQueries.removeLike, filmId, userId);
    }

    @Override
    public boolean existsLike(Long filmId, Long userId) {
        return exists(SqlQueries.checkLikeExists, filmId, userId);
    }
}
