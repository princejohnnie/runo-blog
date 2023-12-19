package dev.levelupschool.backend.dtos;

import dev.levelupschool.backend.model.Article;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.List;

@Relation(collectionRelation = "items")
public class ArticleDto {
    public Long id;
    public String title;
    public String content;
    public String coverUrl;

    public String slug;
    public boolean isPremium;
    public AuthorDto author;
    public List<CommentDto> comments;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public ArticleDto(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.coverUrl = article.getCoverUrl();
        this.author = new AuthorDto(article.getAuthor());
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getUpdatedAt();
        this.isPremium = article.isPremium();
        this.slug = article.getSlug();
        if (article.getComments() != null) {
            this.comments = article.getComments().stream().map(CommentDto::new).toList();
        }
    }
}
