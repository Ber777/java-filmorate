INSERT INTO genres (name)
SELECT genre_name
FROM (VALUES
    ('Комедия'),
    ('Драма'),
    ('Мультфильм'),
    ('Триллер'),
    ('Документальный'),
    ('Боевик')
) AS genres_to_add(genre_name)
WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = genre_name);

INSERT INTO mpa (name)
SELECT mpa_name
FROM (VALUES
    ('G'),
    ('PG'),
    ('PG-13'),
    ('R'),
    ('NC-17')
) AS mpa_to_add(mpa_name)
WHERE NOT EXISTS (SELECT 1 FROM mpa WHERE name = mpa_name);