create table subscriptions (
      id      serial  not null primary key,
      transaction_id   text not null,
      subscription_type text not null,
      start_date timestamp not null default now(),
      end_date timestamp default null,
      is_active boolean not null default false,
      status text not null,
      user_id integer not null references users on delete cascade
);

alter table users add is_premium boolean default false not null;
