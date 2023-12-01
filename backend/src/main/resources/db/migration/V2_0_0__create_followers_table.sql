create table followers (
       follower_id serial not null references users (id),
       followed_id serial not null references users (id)
);

create index followers_index on followers (follower_id, followed_id);
