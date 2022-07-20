CREATE TABLE IF NOT EXISTS "USER" (
    USER_ID     LONG AUTO_INCREMENT PRIMARY KEY,
    EMAIL       VARCHAR(320) NOT NULL,
    LOGIN       VARCHAR(320) NOT NULL,
    USER_NAME   VARCHAR(320) NOT NULL,
    BIRTHDAY    DATE
);

CREATE TABLE IF NOT EXISTS USER_FRIEND (
    OWNER_USER_ID               LONG REFERENCES "USER"(USER_ID) ON DELETE CASCADE,
    FRIEND_ID                   LONG REFERENCES "USER"(USER_ID) ON DELETE CASCADE,
    RELATION_SHIP_STATUS        VARCHAR(100),
    PRIMARY KEY (OWNER_USER_ID,FRIEND_ID)
);

CREATE TABLE IF NOT EXISTS "RATING_MPA" (
   RATING_ID             INT AUTO_INCREMENT PRIMARY KEY,
   RATING_NAME           VARCHAR(10) NOT NULL,
   RATING_DESCRIPTION    VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS "FILM" (
    FILM_ID            LONG AUTO_INCREMENT PRIMARY KEY,
    FILM_NAME          VARCHAR(100) NOT NULL,
    FILM_DESCRIPTION   VARCHAR(200),
    RELEASE_DATE       DATE NOT NULL,
    DURATION           INT,
    RATING_MPA_ID      INT REFERENCES RATING_MPA (RATING_ID)
);

CREATE TABLE IF NOT EXISTS "GENRE" (
     GENRE_ID        INT AUTO_INCREMENT PRIMARY KEY,
     GENRE_NAME      VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS "FILM_GENRE" (
    FILM_ID     LONG REFERENCES FILM(FILM_ID) ON DELETE CASCADE,
    GENRE_ID    LONG REFERENCES GENRE(GENRE_ID),
    PRIMARY KEY (FILM_ID, GENRE_ID)
);

CREATE TABLE IF NOT EXISTS "LIKE" (
    FILM_ID     LONG REFERENCES FILM(FILM_ID) ON DELETE CASCADE,
    USER_ID     LONG REFERENCES "USER"(USER_ID) ON DELETE CASCADE,
    PRIMARY KEY (FILM_ID, USER_ID)
);

CREATE TABLE IF NOT EXISTS "REVIEW" (
    REVIEW_ID       LONG AUTO_INCREMENT PRIMARY KEY,
    REVIEW_CONTENT  VARCHAR(500) NOT NULL,
    IS_POSITIVE     BOOLEAN NOT NULL,
    USER_ID         LONG NOT NULL REFERENCES "USER"(USER_ID) ON DELETE CASCADE,
    FILM_ID         LONG NOT NULL REFERENCES "FILM"(FILM_ID) ON DELETE CASCADE,
    USEFUL          INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS "USEFUL_REVIEW" (
    REVIEW_ID       LONG REFERENCES "REVIEW"(REVIEW_ID) ON DELETE CASCADE,
    USER_ID         LONG REFERENCES "USER"(USER_ID) ON DELETE CASCADE,
    REVIEW_LIKE     BOOLEAN,
    PRIMARY KEY (REVIEW_ID, USER_ID, REVIEW_LIKE)
);


