package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.User;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import java.time.LocalDate;

class UserControllerTest {
    UserController userController = new UserController();

    @Test
    void getUser() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator;
        validator = factory.getValidator();
        LocalDate now = LocalDate.now();
        User user1 = new User(1L, "login1", "name1", "email1@rambler.ru", now);
        userController.create(user1);
        User errUser = new User(1L, null, "name2", "email2rambler.ru", now.plusDays(5));
        Set<ConstraintViolation<User>> violations = validator.validate(errUser);

        assertFalse(violations.isEmpty());
    }
}