--SPRINT 11

DROP TABLE IF EXISTS likes_films;

DROP TABLE IF EXISTS friendship;

DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
    user_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    email     CHARACTER varying(100),
    login     CHARACTER varying(100),
    user_name CHARACTER varying(100),
    birthday  TIMESTAMP
);


CREATE TABLE IF NOT EXISTS friendship
(
    user_id   BIGINT NOT NULL,
    friend_id BIGINT NOT NULL,
    CONSTRAINT pk_friendship_users PRIMARY KEY (user_id, friend_id),
    CONSTRAINT fk_friendship_users_user_id
        FOREIGN KEY (user_id) REFERENCES users,
    CONSTRAINT fk_friendship_users_friend_id
        FOREIGN KEY (friend_id) REFERENCES users
);


CREATE TABLE IF NOT EXISTS ratings
(
    rating_id   INTEGER AUTO_INCREMENT PRIMARY KEY,
    rating_name CHARACTER varying(10)
);


CREATE TABLE IF NOT EXISTS films
(
    film_id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    film_name        CHARACTER varying(150),
    film_description CHARACTER varying(200),
    rating_id        INTEGER,
    release_date     TIMESTAMP,
    duration         INTEGER,
    CONSTRAINT fk_film_rating
        FOREIGN KEY (rating_id) REFERENCES ratings
);


CREATE TABLE IF NOT EXISTS likes_films
(
    film_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_likes_films PRIMARY KEY (film_id, user_id),
    CONSTRAINT fk_likes_films_film_id
        FOREIGN KEY (film_id) REFERENCES films,
    CONSTRAINT fk_likes_films_user_id
        FOREIGN KEY (user_id) REFERENCES users
);


CREATE TABLE IF NOT EXISTS genres
(
    genre_id   INTEGER AUTO_INCREMENT PRIMARY KEY,
    genre_name CHARACTER varying(20)
);


CREATE TABLE IF NOT EXISTS genres_films
(
    film_id  BIGINT  NOT NULL,
    genre_id INTEGER NOT NULL,
    CONSTRAINT pk_genre_films PRIMARY KEY (film_id, genre_id),
    CONSTRAINT fk_genre_films_film
        FOREIGN KEY (film_id) REFERENCES films,
    CONSTRAINT fk_genre_films_genre
        FOREIGN KEY (genre_id) REFERENCES genres
);