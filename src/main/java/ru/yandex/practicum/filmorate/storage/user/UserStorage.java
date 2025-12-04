package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> getAllUsers();

    User getUser(Long id) throws NotFoundException;

    User create(User user);

    User update(User user);

    void checkUserExists(Long id) throws NotFoundException;

    Collection<User> getFriendsOfUser(Long id);

    Collection<User> getCommonFriends(Long id, Long otherId);

    void addFriend(Long id, Long friendId);

    void removeFriend(Long id, Long friendId);

    boolean isEmailUsed(String email);
}
