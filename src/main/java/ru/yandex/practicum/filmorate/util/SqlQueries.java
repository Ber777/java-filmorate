package ru.yandex.practicum.filmorate.util;

public interface SqlQueries {

    /**
     * Query User
     */
    String allUsers = """
      SELECT * FROM users
      """;

    String insertUser = """
      INSERT INTO users (login, username, email, birthday)
      VALUES (?, ?, ?, ?)
      """;

    String updateUser = """
      UPDATE users
      SET login = ?, username = ?, email= ?,  birthday = ?
      WHERE id = ?
      """;

    String byId = """
      SELECT * FROM users WHERE id = ?
      """;

    String checkFriendship = """
      SELECT EXISTS (
          SELECT 1 FROM friendships
          WHERE user_id = ? AND friend_id = ?
      )
      """;

    String addFriend = """
      INSERT INTO friendships(user_id, friend_id)
      VALUES(?, ?)
      """;

    String removeFriend = """
      DELETE FROM friendships
      WHERE user_id = ? AND friend_id = ?
      """;

    String selectFriends = """
      SELECT u.*
      FROM users u
      JOIN friendships f ON u.id = f.friend_id
      WHERE f.user_id = ?;
      """;

    String selectCommonFriends = """
      SELECT u.*
      FROM friendships f1
      JOIN friendships f2 ON f1.friend_id = f2.friend_id
      JOIN users u ON u.id = f1.friend_id
      WHERE f1.user_id = ?
        AND f2.user_id = ?
      """;

    String checkUserExists = """
      SELECT EXISTS(SELECT 1 FROM users WHERE id = ?)
      """;

    String checkEmailUsed = """
      SELECT EXISTS(SELECT 1 FROM users WHERE email = ?)
      """;

    /**
     * Query Mpa
     */
    String AllMpa = """
      SELECT * FROM mpa ORDER BY id
      """;

    String byIdMpa = """
      SELECT * FROM mpa WHERE id = ?
      """;

    /**
     * Query Like
     */
    String checkLikeExists = """
      SELECT COUNT(*) > 0 FROM likes WHERE film_id = ? AND user_id = ?
      """;

    String addLike = """
      INSERT INTO likes(film_id, user_id)
      VALUES(?, ?)
      """;

    String removeLike = """
      DELETE FROM likes
      WHERE film_id = ? AND user_id = ?
      """;

    /**
     * Query Genre
     */
    String allGenre = """
      SELECT * FROM genres ORDER BY id
      """;

    String byIdGenre = """
      SELECT * FROM genres WHERE id = ?
      """;

    /**
     * Query Film
     */
    String allFilms = """
      SELECT
          f.*,
          m.name AS mpa_name,
          (
              SELECT STRING_AGG(CONCAT(g.id, ':', g.name), '|' ORDER BY g.id)
              FROM film_genres fg
              JOIN genres g ON fg.genre_id = g.id
              WHERE fg.film_id = f.id
          ) AS genres_data
      FROM films f
      LEFT JOIN mpa m ON m.id = f.mpa_id
      ORDER BY f.id;
      """;

    String byIdFilm = """
      SELECT
          f.*,
          m.name AS mpa_name,
          (
              SELECT STRING_AGG(CONCAT(g.id, ':', g.name), '|' ORDER BY g.id)
              FROM film_genres fg
              JOIN genres g ON fg.genre_id = g.id
              WHERE fg.film_id = f.id
          ) AS genres_data
      FROM films f
      LEFT JOIN mpa m ON m.id = f.mpa_id
      WHERE f.id = ?
      """;

    String checkFilmExists = """
      SELECT COUNT(*) > 0 FROM films WHERE id = ?
      """;

    String insertFilm = """
      INSERT INTO films (title, description, duration, release_date, mpa_id)
      VALUES (?, ?, ?, ?, ?)
      """;

    String updateFilm = """
      UPDATE films
      SET title = ?, description = ?, duration= ?,  release_date = ?, mpa_id = ?
      WHERE id = ?
      """;

    String deleteFilmGenre = """
      DELETE FROM film_genres
      WHERE film_id = ?
      """;

    String insertFilmGenre = """
      INSERT INTO film_genres(film_id, genre_id)
      VALUES (?, ?)
      """;

    String popularFilm = """
      SELECT
          f.*,
          m.name AS mpa_name,
          COUNT(l.film_id) as likes_count,
          (
              SELECT STRING_AGG(CONCAT(g.id, ':', g.name), '|' ORDER BY g.id)
              FROM film_genres fg
              JOIN genres g ON fg.genre_id = g.id
              WHERE fg.film_id = f.id
          ) AS genres_data
      FROM films f
      LEFT JOIN mpa m ON m.id = f.mpa_id
      LEFT JOIN likes l ON f.id = l.film_id
      GROUP BY
         f.id, f.title, f.description, f.duration, f.release_date, f.mpa_id, m.name
      ORDER BY likes_count DESC
      LIMIT ?;
      """;
}
