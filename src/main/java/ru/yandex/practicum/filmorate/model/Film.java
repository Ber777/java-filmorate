package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

/**
 * Film.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Film {
    public static final int MAX_DESCRIPTION_LENGTH = 200;
    private Long id;

    @NotEmpty(message = "Название фильма не может быть пустым")
    private String name;

    @Size(max = MAX_DESCRIPTION_LENGTH, message = "Максимальная длина описания — 200 символов")
    private String description;

    private LocalDate releaseDate;

    @Min(value = 1, message = "Продолжительность фильма должна быть положительным числом")
    private Integer duration;
}
