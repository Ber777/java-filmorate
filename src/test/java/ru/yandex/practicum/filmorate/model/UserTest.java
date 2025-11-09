package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class UserTest {
    @Test
    void testEquals() {
        LocalDate now = LocalDate.now();
        User user1 = new User();
        user1.setId(1L);
        user1.setLogin("login1");
        user1.setName("name1");
        user1.setEmail("email1@rambler.ru");
        user1.setBirthday(LocalDate.ofYearDay(2000, 1));
        user1.addFriend(1L);

        User user2 = new User();
        user2.setId(1L);
        user2.setLogin("login1");
        user2.setName("name1");
        user2.setEmail("email1@rambler.ru");
        user2.setBirthday(LocalDate.ofYearDay(2000, 1));
        user2.addFriend(1L);

        assertEquals(user1, user2);
    }
}