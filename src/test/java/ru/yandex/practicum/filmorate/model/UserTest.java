package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void testEquals() {
        LocalDate now = LocalDate.now();
        User user1 = new User(1L, "login1", "name1", "email1@rambler.ru", now);
        User user2 = new User(1L, "login1", "name1", "email1@rambler.ru", now);

        assertEquals(user1, user2);
    }
}