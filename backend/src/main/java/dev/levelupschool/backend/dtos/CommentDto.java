package dev.levelupschool.backend.dtos;

import dev.levelupschool.backend.model.Comment;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@Relation(collectionRelation = "items")
public class CommentDto {
    public Long id;
    public String content;
    public AuthorDto author;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.author = new AuthorDto(comment.getAuthor());
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }
}
