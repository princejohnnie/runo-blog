alter table users add created_at timestamp not null default now();

alter table users add updated_at timestamp not null default now();

alter table articles add created_at timestamp not null default now();

alter table articles add updated_at timestamp not null default now();

alter table comments add created_at timestamp not null default now();

alter table comments add updated_at timestamp not null default now();
