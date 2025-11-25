package ru.yandex.practicum.filmorate.service.User;

import ru.yandex.practicum.filmorate.exception.FriendExistsException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUser(Long id) {
        return userStorage.getUser(id);
    }

    public User create(User user) {
        //user.clearFriends();
        user.getFriends().clear();
        return userStorage.create(user);
    }

    public User update(User user) {
        User updatedUser = userStorage.getUser(user.getId());

        if (user.getEmail() != null)
            updatedUser.setEmail(user.getEmail());

        if (user.getBirthday() != null)
            updatedUser.setBirthday(user.getBirthday());

        if (user.getName() != null)
            updatedUser.setName(user.getName());

        if (user.getLogin() != null)
            updatedUser.setLogin(user.getLogin());

        // Чтобы не потерять друзей:
        if (user.getFriends() != null)
            updatedUser.setFriends(user.getFriends());

        return userStorage.update(updatedUser);
    }

    public void addFriend(Long id, Long friendId) {
        if (id.equals(friendId))
            throw new ValidationException(id + " = " + friendId);

        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(friendId);

        if (!user.getFriends().add(friendId))
            throw new FriendExistsException(id, friendId);
        //user.addFriend(friendId);

        if (!friend.getFriends().add(id))
            throw new FriendExistsException(friendId, id);
        //friend.addFriend(id);
    }

    public void removeFriend(Long id, Long friendId) {
        User user1 = userStorage.getUser(id);
        User user2 = userStorage.getUser(friendId);

        //user1.removeFriend(friendId);
        user1.getFriends().remove(friendId);

        //user2.removeFriend(id);
        user2.getFriends().remove(id);
    }

    public Set<User> getCommonFriends(Long id, Long otherId) {
        if (id.equals(otherId))
            return Set.of();

        Set<Long> friends1 = userStorage.getUser(id).getFriends();
        Set<Long> friends2 = userStorage.getUser(otherId).getFriends();

        return friends1.stream()
                .filter(friends2::contains)
                .map(userStorage::getUser)
                .collect(Collectors.toSet());
    }

    public Collection<User> getFriends(Long id) {
        User user = userStorage.getUser(id);
        return user.getFriends().stream()
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }
}