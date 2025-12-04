package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FriendExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.base.BaseStorage;

import java.util.Collection;
import java.util.Optional;
import ru.yandex.practicum.filmorate.util.SqlQueries;

@Repository("userDbStorage")
public class UserDbStorage extends BaseStorage<User> implements UserStorage {

    public UserDbStorage(JdbcTemplate jdbcTemplate, UserRowMapper rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    @Override
    public Collection<User> getAllUsers() {
        return getMany(SqlQueries.allUsers);
    }

    @Override
    public User create(User user) {
        Long id = insertAndReturnId(SqlQueries.insertUser,
                Long.class,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday()
        );

        if (id == null) {
            return null;
        }
        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        int updated = super.update(SqlQueries.updateUser,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());
        if (updated > 0) {
            return user;
        }
        return null;
    }

    @Override
    public void checkUserExists(Long id) throws NotFoundException {
        boolean exists = exists(SqlQueries.checkUserExists, id);
        if (!exists) {
            throw new NotFoundException("Пользователь id:" + id + " не найден");
        }
    }

    @Override
    public User getUser(Long id) {
        Optional<User> one = getOne(SqlQueries.byId, id);
        if (one.isEmpty()) {
            throw new NotFoundException("Пользователь id:" + id + " не найден");
        }
        return one.get();
    }

    @Override
    public boolean isEmailUsed(String email) {
        return exists(SqlQueries.checkEmailUsed, email);
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        checkUserExists(id);
        checkUserExists(friendId);

        Boolean alreadyAdded = jdbcTemplate.queryForObject(SqlQueries.checkFriendship,
                Boolean.class,
                id,
                friendId
        );

        if (Boolean.TRUE.equals(alreadyAdded)) {
            throw new FriendExistsException(id, friendId);
        }

        int updatedRows = update(SqlQueries.addFriend, id, friendId);
    }

    @Override
    public void removeFriend(Long id, Long friendId) {
        checkUserExists(id);
        checkUserExists(friendId);
        int updatedRows = update(SqlQueries.removeFriend, id, friendId);
    }

    @Override
    public Collection<User> getFriendsOfUser(Long id) {
        checkUserExists(id);
        return getMany(SqlQueries.selectFriends, id);
    }

    @Override
    public Collection<User> getCommonFriends(Long id, Long otherId) {
        checkUserExists(id);
        checkUserExists(otherId);
        return getMany(SqlQueries.selectCommonFriends, id, otherId);
    }
}