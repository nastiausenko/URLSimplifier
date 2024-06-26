CREATE TABLE users
(
    id       UUID PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(50) DEFAULT 'USER' CHECK (role IN ('USER', 'ADMIN'))
);

CREATE TABLE links
(
    id              UUID PRIMARY KEY,
    long_link       VARCHAR(1000)                         NOT NULL,
    short_link      VARCHAR(50)                           NOT NULL UNIQUE,
    user_id         UUID                                  NOT NULL,
    created_time    TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    expiration_time TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    statistics      INT         DEFAULT 0 CHECK (statistics >= 0),
    status          VARCHAR(50) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE', 'DELETED')),
    FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE
);
