create table tokens (
       id      serial  not null primary key,
       token   text not null,
       expiry_time timestamp not null default now(),
       user_id integer not null references users on delete cascade
);
