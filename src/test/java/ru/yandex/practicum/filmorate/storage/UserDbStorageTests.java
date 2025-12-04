package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserRowMapper;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, UserRowMapper.class, LikeDbStorage.class, FilmDbStorage.class, FilmRowMapper.class})
public class UserDbStorageTests {

    private final UserDbStorage storage;
    private final User user = new User("User", "Test Name", "test@rambler.ru",
            LocalDate.of(1996, 6, 7));

    @Test
    public void createUser() {
        int count = storage.getAllUsers().size();

        User created = storage.create(user);

        Assertions.assertNotNull(created.getId());
        Assertions.assertEquals(count + 1, storage.getAllUsers().size());
    }

    @Test
    public void getUser() {
        User created = storage.create(user);
        User found = storage.getUser(created.getId());

        Assertions.assertEquals(user.getName(), found.getName());
        Assertions.assertEquals(user.getLogin(), found.getLogin());
        Assertions.assertEquals(user.getEmail(), found.getEmail());
        Assertions.assertEquals(user.getBirthday(), found.getBirthday());
    }

    @Test
    void getNonExisting() {
        Long notExistId = 100000L;
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> storage.getUser(notExistId)
        );
        assertThat(exception.getMessage()).contains("Пользователь id:100000 не найден");
    }

    @Test
    public void testUpdateUser() {
        User created = storage.create(user);

        created.setLogin("new_login");
        created.setName("new_name");
        created.setEmail("new_test@rambler.ru");

        storage.update(created);
        User found = storage.getUser(created.getId());

        Assertions.assertEquals("new_login", found.getLogin());
        Assertions.assertEquals("new_name", found.getName());
        Assertions.assertEquals("new_test@rambler.ru", found.getEmail());
    }

}
