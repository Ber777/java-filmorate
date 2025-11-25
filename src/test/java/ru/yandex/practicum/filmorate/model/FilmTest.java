package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.Collections;

class FilmTest {
    @Test
    void testEquals() {
        LocalDate now = LocalDate.now();
        Film film1 = new Film();
        film1.setId(1L);
        film1.setName("name1");
        film1.setDescription("description1");
        film1.setReleaseDate(now);
        film1.setDuration(10);
        film1.setLikes(Collections.singleton(2L));
        //film1.addLike(2L);

        Film film2 = new Film();
        film2.setId(1L);
        film2.setName("name1");
        film2.setDescription("description1");
        film2.setReleaseDate(now);
        film2.setDuration(10);
        film2.setLikes(Collections.singleton(2L));
        //film2.addLike(2L);

        assertEquals(film1, film2);
    }
}