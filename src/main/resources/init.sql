DROP TABLE IF EXISTS users;
CREATE TABLE users(
                      id SERIAL NOT NULL,
                      username VARCHAR(255) NOT NULL,
                      password VARCHAR(255) NOT NULL,
                      money INTEGER NOT NULL,
                      PRIMARY KEY (id)
);

COMMENT ON TABLE users IS '用户表';
COMMENT ON COLUMN users.id IS 'id';
COMMENT ON COLUMN users.username IS 'username';
COMMENT ON COLUMN users.password IS 'password';
COMMENT ON COLUMN users.money IS 'money';


CREATE UNIQUE INDEX username_unique ON users(username);

DROP TABLE IF EXISTS packages;
CREATE TABLE packages(
                         id VARCHAR(36) NOT NULL,
                         name VARCHAR(32) NOT NULL,
                         damage NUMERIC(24,1) NOT NULL,
                         PRIMARY KEY (id)
);

COMMENT ON TABLE packages IS '包';
COMMENT ON COLUMN packages.id IS 'id';
COMMENT ON COLUMN packages.name IS 'name';
COMMENT ON COLUMN packages.damage IS 'Damage';

