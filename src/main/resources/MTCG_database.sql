-- auto-generated definition
create table users
(
    id       serial
             primary key,
    username varchar(255) not null,
    password varchar(255) not null,
    money    integer      not null
);

comment on table users is 'username';

comment on column users.id is 'id';

comment on column users.username is 'username';

comment on column users.password is 'password';

comment on column users.money is 'money';

alter table users
    owner to swe1user;


DROP TABLE IF EXISTS packages;
CREATE TABLE packages(
                         p-id VARCHAR(36) NOT NULL,
                         name VARCHAR(36) NOT NULL,
                         damage numeric(24, 1) not null,
                         price INTEGER NOT NULL,
                         UPDATED_TIME TIMESTAMP NOT NULL,
                         PRIMARY KEY (p-id)
);

COMMENT ON TABLE packages IS '';
COMMENT ON COLUMN packages.p-id IS 'package id';
comment on column packages.damage is 'Damage';
COMMENT ON COLUMN packages.name IS 'buyer';
COMMENT ON COLUMN packages.price IS 'package price';
COMMENT ON COLUMN packages.UPDATED_TIME IS 'created time';

