alter table articles drop column is_premium;
alter table articles add is_premium boolean default false not null;
