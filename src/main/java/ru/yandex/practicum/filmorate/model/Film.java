package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

@Data
@NoArgsConstructor
public class Film {
    private Long id;
    private String name;
    private String description;
    private Integer duration;
    private LocalDate releaseDate;
    private Mpa mpa;

    // List.of() - не рекоммендуется, запомню, спасибо!
    private Collection<Genre> genres = new HashSet<>();  // = List.of();

    public Film(String name, String description, Integer duration, LocalDate releaseDate, Mpa mpa) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.mpa = mpa;
    }
}
