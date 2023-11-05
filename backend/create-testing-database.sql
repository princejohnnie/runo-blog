create table users (
    id   serial not null primary key,
    name text   not null
);

create table articles (
    id      serial  not null primary key,
    title   text null default null,
    content text null default null,
    user_id integer not null references users on delete cascade
);

create table comments (
    id         serial  not null primary key,
    content    text null default null,
    user_id    integer not null references users on delete cascade,
    article_id integer not null references articles on delete cascade
);
