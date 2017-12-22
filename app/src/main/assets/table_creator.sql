CREATE TABLE users
(
    id integer PRIMARY KEY AUTOINCREMENT,
    name text,
    email text,
    photo text,
    oid text,
    active integer,
    updated_at integer
);

CREATE TABLE programs
(
    id integer PRIMARY KEY AUTOINCREMENT,
    name text,
    description text,
    points integer,
    user_id text,
    user_oid text,
    oid text,
    active integer,
    updated_at integer,
    FOREIGN KEY(user_id) REFERENCES users(id)
);



