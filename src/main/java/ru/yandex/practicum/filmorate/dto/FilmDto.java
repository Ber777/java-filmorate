package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Create;
import ru.yandex.practicum.filmorate.model.Update;

import java.time.LocalDate;
import java.util.Collection;

@Data
public class FilmDto {
    public static final int MAX_DESCRIPTION_LENGTH = 200;

    @NotEmpty(groups = Update.class)
    private Long id;

    @NotEmpty(message = "Название фильма не может быть пустым")
    private String name;

    @Size(max = MAX_DESCRIPTION_LENGTH, message = "Максимальная длина описания — 200 символов")
    private String description;

    @Min(value = 1, message = "Продолжительность фильма должна быть положительным числом")
    private Integer duration;

    @Past
    @NotEmpty(groups = Create.class)
    private LocalDate releaseDate;

    @NotEmpty(groups = Create.class)
    private MpaDto mpa;

    private Collection<GenreDto> genres = null;
}
