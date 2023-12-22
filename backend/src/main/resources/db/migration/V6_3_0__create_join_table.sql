create table article_categories (
     article_id BIGINT REFERENCES articles(id),
     category_id BIGINT REFERENCES categories(id),
    PRIMARY KEY (article_id, category_id)
);
