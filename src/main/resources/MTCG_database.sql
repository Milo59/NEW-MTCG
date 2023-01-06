create table users
(
    id       serial
        primary key,
    username varchar(255)        not null,
    password varchar(255)        not null,
    money    integer             not null,
    name     varchar(255),
    bio      varchar(255),
    image    varchar(255),
    scored   integer default 100 not null
);

comment on table users is '用户表';

comment on column users.id is 'id';

comment on column users.username is 'username';

comment on column users.password is 'password';

comment on column users.money is 'money';

alter table users
    owner to swe1user;

create unique index username_unique
    on users (username);



create table packages
(
    id           varchar(36)       not null
        primary key,
    name         varchar(36)       not null,
    price        integer default 5 not null,
    state        integer default 1 not null,
    updated_time timestamp
);

alter table packages
    owner to swe1user;

create table card
(
    id     varchar(36)    not null
        primary key,
    u_id   integer
        references users,
    p_id   varchar(36)    not null
        references packages,
    damage numeric(24, 1) not null,
    name   varchar(255)   not null,
    deck   integer default 0
);

alter table card
    owner to swe1user;



create table trades
(
    id            varchar(36) not null
        primary key,
    u_id          integer
        references users,
    cardtotrade   varchar(90) not null,
    type          varchar(36) not null,
    minimumdamage integer     not null,
    status        integer default 0
);

alter table trades
    owner to swe1user;



create table battle_log
(
    id                serial
        primary key,
    user1             varchar(255)   not null,
    user2             varchar(255)   not null,
    winner            varchar(255)   not null,
    user1_card_name   varchar(36)    not null,
    user1_card_damage numeric(24, 1) not null,
    user2_card_name   varchar(36)    not null,
    user2_card_damage numeric(24, 1) not null
);

comment on table battle_log is 'battle_log';

comment on column battle_log.id is 'id';

comment on column battle_log.user1 is 'user1';

comment on column battle_log.user2 is 'user2';

comment on column battle_log.winner is 'winner';

comment on column battle_log.user1_card_name is 'user1_card';

comment on column battle_log.user1_card_damage is 'user1_card_damage';

comment on column battle_log.user2_card_name is 'user2_card_name';

comment on column battle_log.user2_card_damage is 'user2_card_damage';

alter table battle_log
    owner to swe1user;

