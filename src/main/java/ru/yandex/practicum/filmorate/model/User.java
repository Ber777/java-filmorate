package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * User.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private Long id;

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
