package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.EmailExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.*;
import org.springframework.stereotype.Component;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User create(User user) {
        if (emails.contains(user.getEmail()))
            throw new EmailExistsException(user.getEmail());

        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());

        user.setId(getNextId());
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        return user;
    }

    @Override
    public User update(User user) {
        User oldUser = getUser(user.getId());

        String oldEmail = oldUser.getEmail();
        String newEmail = user.getEmail();

        if (newEmail != null
                && !newEmail.equals(oldEmail)
                && emails.contains(newEmail)) {
            throw new EmailExistsException(newEmail);
        }

        users.put(user.getId(), user);
        emails.remove(oldEmail);
        emails.add(newEmail);

        return user;
    }

    @Override
    public User getUser(Long id) {
        User user = users.get(id);
        if (user == null)
            throw new NotFoundException("Пользователь id:" + id + " не найден");
        return user;
    }

    private long getNextId() {
        long maxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++maxId;
    }
}
