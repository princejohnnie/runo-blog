create table bookmarks (
    bookmarker_id serial not null references users (id),
    bookmarked_id serial not null references articles (id)
);

create index bookmarks_index on bookmarks (bookmarker_id, bookmarked_id);
