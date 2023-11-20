package dev.levelupschool.backend.repository;

import dev.levelupschool.backend.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
