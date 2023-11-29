package dev.levelupschool.backend.dtos;

import dev.levelupschool.backend.model.Article;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@Relation(collectionRelation = "items")
public class ArticleDto {
    public Long id;
    public String title;
    public String content;
    public AuthorDto author;
    public List<CommentDto> comments;

    public ArticleDto(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.author = new AuthorDto(article.getAuthor());

        if (article.getComments() != null) {
            this.comments = article.getComments().stream().map(CommentDto::new).toList();
        }
    }
}
