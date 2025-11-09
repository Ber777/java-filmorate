package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.exception.LikeExistsException;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotEmpty;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Film.
 */
//@AllArgsConstructor
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

    private Set<Long> likes = new LinkedHashSet<>();

    public void addLike(Long userId) {
        if (!likes.add(userId))
            throw new LikeExistsException(id, userId);
    }

    public void removeLike(Long userId) {
        if (!likes.remove(userId))
            throw new LikeNotFoundException(id, userId);
    }

    public void clearLikes() {
        likes.clear();
    }
}
