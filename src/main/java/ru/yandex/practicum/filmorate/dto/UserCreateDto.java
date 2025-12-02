package ru.yandex.practicum.filmorate.dto;

import ru.yandex.practicum.filmorate.model.Create;
import ru.yandex.practicum.filmorate.model.Update;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserCreateDto {

    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "^\\S*$", message = "Логин не может содержать пробелы", groups = {
            Create.class,
            Update.class
    })
    private String login;

    private String name;

    @NotEmpty(message = "Электронная почта не может быть пустой", groups = Create.class)
    @Email(message = "Электронная почта должна содержать символ @", groups = {
            Create.class,
            Update.class
    })
    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "Дата рождения не может быть в будущем", groups = Create.class)
    private LocalDate birthday;
}
