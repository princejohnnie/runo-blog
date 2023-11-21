create table users (
    id serial not null primary key,
    email varchar(255) not null,
    name text not null,
    slug text,
    password varchar(255) not null
);

create table articles (
    id      serial  not null primary key,
    title   text null default null,
    content text null default null,
    author_id integer not null references users on delete cascade
);

create table comments (
    id         serial  not null primary key,
    content    text null default null,
    author_id    integer not null references users on delete cascade,
    article_id integer not null references articles on delete cascade
);
